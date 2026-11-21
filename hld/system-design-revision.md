# System Design – Revision Sheet

## Contents

- [Message Queue](#message-queue)
- [Caching](#caching)
- [Database Sharding](#database-sharding)
- [Data Modeling](#data-modeling)
- [API Design](#api-design)
- [Object Storage (Blob Storage)](#object-storage-blob-storage)


## Message Queue

- **Delivery Guarantees** – The promise a queue makes about how many times a message will reach the
  consumer (at-most-once, at-least-once, or exactly-once).
  - **At-most-once** – Fire and forget: each message is delivered zero or one time, so it's fast but
    messages can be lost.
  - **At-least-once** – The consumer acknowledges only after processing and the broker redelivers
    anything unacknowledged, so no message is lost but duplicates can occur, which means consumers
    must be idempotent.
    - **Idempotent Consumer** – Processing the same message more than once gives the same result as
      processing it once.
  - **Exactly-once** – Every message is processed exactly one time; true end-to-end exactly-once is
    practically impossible, and only limited forms are achievable (e.g. via deduplication or
    transactions) with great difficulty and cost.

### When to Use a Queue

- **Async Work** – Offload slow or non-urgent tasks so the caller can respond immediately without
  waiting for them to finish.
- **Bursty Traffic** – Absorb sudden spikes by buffering requests in the queue so none are dropped
  while consumers catch up.
- **Decoupling** – Producers and consumers don't depend on each other directly, so each service can
  be scaled independently.
- **Reliability** – When work can't be lost, the queue holds messages durably until they're
  successfully processed.

- **Scaling a Queue (High Throughput)** – A common interview question; the answer is partitioning.
  - **Partition** – A sub-queue (logical queue) inside a queue; adding partitions (horizontal
    scaling) lets multiple consumers read in parallel, increasing throughput.
  - **Consumer Group** – A set of consumers that share the work of a topic, where each partition is
    read by only one consumer in the group, so messages are processed in parallel without
    duplication.
    - **Consumer Ceiling** – Max useful consumers in a group = number of partitions; any extra
      consumers sit idle.
  - **Partition Key** – Messages with the same key (e.g. user ID) always go to the same partition,
    so one consumer processes them in order.
    - *Example:* A user's +$100 credit and −$50 debit share a key, so the credit is processed first;
      on different partitions the debit could run first and be declined for insufficient balance.
    - **Choosing the Key** – Pick a key that spreads load evenly across partitions; a skewed key
      creates a *hot partition* that becomes the bottleneck.

- **Back Pressure** – When producers outpace consumers, the queue signals them to slow down (by
  rejecting, erroring or blocking with a "we're full" response) instead of letting the backlog grow
  without limit.

- **DLQ (Dead Letter Queue)** – A separate queue where messages that keep failing after max retries
  are moved, so they don't block the main queue and can be inspected or replayed later.
  - **Why** – Retrying forever is pointless; repeated failures usually mean a malformed message
    (*poison message*) or an underlying issue, so after N retries it's parked in the DLQ.

- **What if the Queue Goes Down?** – Modern queues persist messages to disk and replicate each
  partition across brokers; if the leader broker dies, a replica with the same data is promoted to
  leader (*failover*) and takes over.

- **Retention Period** – How long the queue keeps messages (by time or size, e.g. 7 days) before
  deleting them, which lets consumers replay old messages or catch up after downtime.
  - **Log-based (Kafka)** – Retention applies to the on-disk log; messages stay until retention
    expires even after being consumed, which enables replay.
  - **Traditional (SQS, RabbitMQ)** – Messages are deleted once consumed and acked; retention only
    caps how long an unconsumed message can sit before being dropped.

### Types of Queues (Examples)

- **Kafka** – Distributed, partitioned log built for very high throughput and event streaming, where
  consumers pull and can replay messages within retention.
- **Amazon SQS** – Fully managed AWS queue with no servers to run; *Standard* gives at-least-once
  with best-effort ordering, *FIFO* gives strict ordering and deduplication.
- **RabbitMQ** – Traditional message broker (AMQP) with flexible routing via exchanges, pushing
  messages to consumers and deleting them once acked.

## Caching

- **Cache** – Temporary, fast storage (usually in memory) that keeps recently or frequently used
  data handy so it can be retrieved faster than from the main data source.

- **External Cache** – A separate, shared cache service (e.g. Redis, Memcached) that many
  horizontally scaled app servers talk to; reads are much faster than the DB, which acts as the
  fallback on a cache miss.

- **In-Process Cache** – Part of the app server's own memory is used as a cache; it's the fastest
  option (no network hop), but each server holds its own copy, so it isn't shared across servers and
  is lost if that server goes down.
  - *Interview tip:* Usually bring it up only for low-level optimizations, not as the default
    answer.

```
 External Cache (shared)            In-Process Cache (per server)

 [App 1] [App 2] [App 3]            [App 1]     [App 2]     [App 3]
    \       |       /               [cache]     [cache]     [cache]
     v      v      v                    \          |          /
   [ Redis / Memcached ]                 \         |         /
            | miss                        v        v        v
            v                            [        DB         ]
         [  DB  ]
```

- **CDN (Content Delivery Network)** – A network of cache servers (*edge locations*) spread across
  regions; each user's request is served by the edge closest to them, cutting network latency.
  - **What it caches** – Historically static media (images, video, JS/CSS), but modern CDNs also
    cache API responses and dynamic content, and can run code at the edge.
  - *Interview tip:* Still position the CDN for static media (images, videos, JS/CSS).

- **Client-Side Cache** – Data cached on the client itself (browser storage, HTTP cache, mobile app
  memory); the fastest option since the request never leaves the device.
  - **Downsides** – Stale data that's hard to invalidate from the server, limited storage, and no
    control once data is on the user's device.
  - *Interview tip:* Rarely comes up.

### Cache Architecture (Patterns)

- **Cache-Aside (Lazy Loading)** – The app checks the cache first and returns on a hit; on a miss it
  reads the DB, writes the result into the cache, then returns it.
  - **Downside** – A cache miss costs extra latency (cache check, then DB read, then cache write).
  - *Interview tip:* The most popular pattern and the default answer.

    ```
    App ──1. get──▶ Cache ──hit──▶ return
     │
     └─ miss ─▶ 2. read DB ─▶ 3. set Cache ─▶ return
    ```

- **Write-Through** – Every write goes to the cache and the DB together (synchronously), so the
  cache always holds fresh data and reads right after a write are hits.
  - **Downside** – Slower writes (two writes before acking) and higher cost, since the cache fills
    with data that may never be read.
  - **Dual-Write Problem** – The two writes aren't atomic, so if the cache write succeeds but the DB
    write fails (or vice versa), the cache and DB become inconsistent.
  - *Interview tip:* Use when reads must see the latest data immediately; often paired with
    cache-aside for reads.
  - **Implementation** – Caches like Redis/Memcached don't natively push writes to the DB, so the
    app or a library handles both writes (e.g. Spring Cache's `@CachePut`, Ehcache's `CacheWriter`).

- **Write-Behind (Write-Back)** – The app writes only to the cache and gets an immediate ack; the
  cache asynchronously flushes the data to the DB later, often in batches.
  - **Upside** – Very fast writes and fewer DB hits, good for write-heavy workloads.
  - **Downside** – Data loss if the cache dies before flushing, and the DB is temporarily behind the
    cache.

- **Read-Through** – Like cache-aside, but on a miss the cache itself fetches the data from the DB,
  stores it and returns it, so the app only ever talks to the cache.
  - *Example:* This is how a CDN works: on a miss, the edge fetches from the origin server, caches
    the content and serves it.


- **Cache Eviction Policies** – Rules for deciding which data to remove when the cache is full, to
  make room for new entries.
  - **LRU (Least Recently Used)** – Evicts the item that hasn't been accessed for the longest time.
    - *Interview tip:* The default choice.
  - **LFU (Least Frequently Used)** – Evicts the item accessed the fewest times; best for skewed
    access patterns, where you want to keep the most-accessed (hot) data.
  - **FIFO (First In, First Out)** – Evicts the oldest inserted item, regardless of how often or
    recently it was used.
  - **TTL (Time To Live)** – Each item expires after a set time; used when data freshness matters.

### Common Caching Issues

- **Cache Stampede (Thundering Herd)** – A popular key expires or is evicted, and right after,
  thousands of requests miss at once; they all hit the DB together (overloading it) and each tries
  to write the same data back into the cache.
  - **Why it happens** – Every request arriving in the window before the first one refills the cache
    also misses, so it's worst for very hot keys and expensive DB queries.
  - **Fix 1: Request Coalescing** – Only one request fetches from the DB and refills the cache while
    the others wait, then all of them are served from the freshly cached value.
  - **Fix 2: Cache Warming** – Refresh popular keys before they expire (reset the TTL or re-add the
    key proactively), so a hot key never goes empty.

- **Cache Consistency** – Keeping the cache and DB in sync; when data changes in the DB but the
  cache still holds the old value, users read stale data.
  - **Why it happens** – Most systems read from the cache but write to the DB, so after a write the
    cached copy can lag behind. This creates an inconsistency window where cache and DB hold
    different values until the stale entry is evicted or expires (depends on the eviction
    policy/TTL).
  - **No perfect fix** – The approaches below only reduce the window.
  - **Fix 1: Invalidate on Write** – Whenever a key is written to the DB, delete it from the cache,
    so the next read misses and reloads the fresh value.
  - **Fix 2: Short TTL** – Keep TTLs short so stale entries expire quickly, at the cost of more
    cache misses and DB load.
  - **Fix 3: Accept Eventual Consistency** – In many cases a brief delay is fine, so you accept it.
    *Example:* a profile picture update showing up for others a minute or two later.

- **Hot Keys** – One key gets far more traffic than the rest (e.g. a celebrity's profile), so the
  single cache node holding it gets overloaded even when the cluster as a whole has spare capacity.
  - **Fix 1: Replicate Hot Keys** – Copy the hot key to all (or several) cache nodes and spread
    reads across them.
  - **Fix 2: Local (In-Process) Cache** – Keep hot keys in each app server's own memory, so most
    reads never reach the cache cluster.

## Database Sharding

- **Sharding** – Splitting a database's data across multiple database instances (*shards*), where
  each shard holds a subset and all shards together make up the complete dataset.
  - **Why Shard** – A single DB eventually can't handle the load and data volume; vertical scaling
    (bigger machine) hits a ceiling as the company grows, while sharding scales horizontally by
    simply adding another shard when volume increases.

- **Challenges of Sharding** – Sharding solves scale but introduces new problems to design for.
  - **How to Split the Data?** – The first decision: choosing how data is divided across shards (the
    *shard key* and *sharding strategy*).
    - **Shard Key** – The field used to decide which shard a record goes to. *Example:* shard by
      user ID, and all data for that user lives on the same shard.
      - **Choosing a Good Shard Key**

        | Good | Bad |
        |---|---|
        | **High cardinality** – many distinct values (e.g. user ID), so data can be split finely | **Low cardinality** – few distinct values (e.g. country, status), so data clumps into a few shards |
        | **Even distribution** – load and data spread evenly across shards | **Unevenly distributed** – some values far more common, creating hot shards |
        | **Aligns with queries** – common queries hit a single shard | **Queries require scatter-gather** – queries must fan out to every shard and merge results |

        - **Good examples (e-commerce)** – `user_id` and `order_id`: high cardinality, spread
          evenly, and most queries are per-user or per-order.
        - **Bad example: `is_premium`** – Only two values (premium / non-premium), so very low
          cardinality, which means at most 2 useful shards and they're likely uneven.
        - **Bad example: `created_at` (by year)** – Most traffic hits the latest year, so one shard
          takes nearly all the load (*hot shard*) and sharding doesn't solve the problem.

  - **How to Distribute Data? (Sharding Strategies)** – Once you have a shard key, the strategy
    decides which shard each key value maps to.
    - **Range-Based** – Each shard owns a contiguous range of key values (e.g. user IDs 1–1M → Shard
      A, 1M–2M → Shard B).
      - **Upside** – Simple, and range queries (e.g. "orders from last week") hit only one or a few
        shards.
      - **Downside** – Uneven load: new or popular ranges (e.g. the latest IDs or dates) all land on
        one shard, creating a *hot shard*. *Example:* sharding by user ID, where the most active
        users are new ones, so the latest shard takes most of the load.
      - *Interview tip:* Rarely used in practice for this reason.
    - **Hash-Based (Simple Hash / Modulo)** – Run a hash function over the shard key and take
      `hash(key) % N` (N = number of shards) to pick the shard.
      - **Upside** – Spreads data and load evenly across shards.
      - **Downside** – Range queries must hit every shard, and adding or removing a shard is a
        nightmare: changing N remaps almost every key, so nearly all data must be reshuffled.
        Consistent hashing solves this.
    - **Consistent Hashing** – Shards and keys are placed on a hash ring, and each key goes to the
      next shard clockwise; adding or removing a shard only moves the keys next to it.
      - **Upside** – Even distribution like hash-based, but resharding moves only ~1/N of the data.
        *Interview tip:* the go-to answer for "how do you add shards without moving everything?"
    - **Directory-Based (Lookup Table)** – A separate lookup service stores which shard each key (or
      key group) lives on.
      - **Upside** – Most flexible: data can be moved or rebalanced just by updating the table.
      - **Downside** – Every query needs an extra DB hop (first look up the shard, then fetch the
        data), and the lookup table is a single point of failure.
      - *Interview tip:* Almost never the right answer.

    ```
    Range:   [1–1M] → A    [1M–2M] → B    [2M–3M] → C
    Hash:    shard = hash(user_id) % 3   →  A | B | C
    Ring:        A
               /   \        key → next shard clockwise
              C ─── B       add D between A & B → only keys between A and D move (B → D)
    ```

  - **Hotspots** – One shard gets far more traffic or data than the others (because of a skewed key
    or a viral entity), so it becomes the bottleneck while the other shards sit underused.
    - **Why it happens** – The *celebrity problem*: a celebrity's profile and related data get far
      more views than a normal user's, so the shard holding them is overloaded.
    - **Fix 1: Compound Shard Key (Key Salting)** – Spread a hot entity's data across several shards
      by adding a suffix to its key (e.g. `celebrity_id#1…#N`), so requests for it are distributed
      instead of hitting one shard.
    - **Fix 2: Dedicated Hot Shard** – Move hot entities (celebrities) onto their own separate,
      bigger shard, and use a small directory-style lookup table to route their requests there.
  - **Cross-Shard Operations** – Queries, joins or transactions that need data from multiple shards
    must fan out (*scatter-gather*) and merge results, which is slow and complex.
    - **Why it happens** – Even with a good shard key, some queries still need data from multiple
      shards.
    - **Fix: Cache the Aggregated Result** – Run the scatter-gather once, store the merged result in
      Redis with a TTL, and serve later requests from the cache, trading a short window of staleness
      (eventual consistency) for speed. The delay is usually small and acceptable in most cases.
    - **Fix: Denormalization** – Duplicate commonly needed data across shards, so a query finds
      everything on one shard and avoids cross-shard calls. The cost is extra storage and keeping
      the copies in sync on writes.
  - **Maintaining Consistency** – Keeping data consistent when one operation updates records on
    different shards, since a single DB transaction can't span shards.
    - **Why it happens** – On a single DB, an operation touching several records can be made atomic
      (all-or-nothing) with one transaction; across shards that guarantee is lost, so one shard's
      update can succeed while another's fails.
    - **Two-Phase Commit (2PC)** – The textbook fix: a coordinator first asks every shard to
      *prepare* (lock and vote yes/no), then tells all of them to *commit* only if every shard voted
      yes, otherwise all *abort*.
      - **Downside** – Slow (several network round trips, with locks held throughout), and the
        coordinator is a single point of failure that can leave shards stuck if it dies mid-way.
      - *Interview tip:* Almost never the right answer in practice.
    - **Saga Pattern** – The practical fix: break the operation into a sequence of local
      transactions, one per shard; if a later step fails, run *compensating transactions* to undo
      the earlier steps (e.g. refund a debit).
      - **Trade-off** – No locks across shards, so it's fast and resilient, but the data is only
        eventually consistent while the saga runs.
      - *Interview tip:* First try to avoid the problem by picking a shard key that keeps related
        data on the same shard; if a cross-shard write is truly unavoidable, Saga is the standard
        answer.

- **Interview Tip: Don't Shard by Default** – First estimate your data size and write load; only
  shard if you'll outgrow a single DB.
  - **Single-instance ballpark** – One well-provisioned Postgres instance can handle roughly ~140 TB
    of data and ~15K writes/sec (rule of thumb; real limits depend on hardware and workload).
  - **Try first** – Vertical scaling, read replicas (for read-heavy load) and caching; reach for
    sharding only when you're beyond these limits.

## Data Modeling

- **Data Modeling** – Defining which entities exist, how each is identified (keys) and how they
  relate. In interviews it shows up twice: listing *core entities* during requirements, and
  sketching a minimal schema next to the DB in the high-level design.
  - *Interview tip:* Don't over-model. Show key fields, keys, relationships and indexes, tie each
    choice to requirements, and move on.

### Database Types

- **Relational (SQL)** – Tables with fixed schemas, joins, foreign keys and *ACID* transactions
  (PostgreSQL, MySQL).
  - *Interview tip:* The default choice, especially when strong consistency matters (no
    double-charging, no overselling). Default to PostgreSQL.
- **Document** – Flexible JSON-like documents that *embed* related data instead of joining (MongoDB,
  Firestore); good for nested or frequently changing schemas, but updating embedded data is harder.
  Rarely the right pick.
- **Key-Value** – Exact-key lookups only, very fast (Redis, DynamoDB, Memcached); for caches,
  sessions and feature flags, usually in front of SQL as the source of truth.
- **Wide-Column** – Rows grouped by a *partition key* with flexible columns, built for append-heavy
  writes and range reads (Cassandra, HBase); for time-series, logs, telemetry and IoT. Model it
  around query patterns.
- **Graph** – Nodes and edges for relationship traversal (Neo4j, Neptune). *Interview tip:* Almost
  never the right answer; even Facebook stores its social graph in MySQL.

- **Schema Drivers** – Three factors to tie every schema decision to:
  - **Data Volume** – Decides whether data fits in one store or must be split.
  - **Access Patterns** – The most important driver: derive the queries from each API endpoint.
  - **Consistency Requirements** – Strong consistency points to one ACID database; eventual
    consistency allows spreading data across systems.

### Keys & Relationships

- **Primary Key (PK)** – Uniquely identifies a record; use stable *system-generated IDs*
  (`user_id`), not mutable business data like email.
- **Foreign Key (FK)** – References another table's PK and enforces *referential integrity* (no
  orphaned rows), at the cost of validation on every write. Some very large companies drop FKs and
  enforce integrity in the app; mentioning that trade-off shows depth.
- **Constraints** – `NOT NULL`, `UNIQUE`, `CHECK` enforce correctness in the DB, with some write
  overhead.
- **One-to-Many (1:N)** – Parent owns many children (user → posts).
- **Many-to-Many (N:M)** – Needs a *junction (linking) table* (`likes` between users and posts).
- **One-to-One (1:1)** – Rare; often a sign the two tables should be merged.

- **Indexing for Access Patterns** – Every index should back a real query. *Example:* `GET
  /users/{id}/posts` needs an index on `posts.user_id`.
  - **Composite Index** – An index on multiple columns, e.g. `(user_id, created_at)`, to fetch a
    user's recent posts without scanning.

- **Normalization** – Each fact is stored in exactly one place, avoiding *update anomalies*.
  - *Interview tip:* Start normalized and denormalize only when needed.
- **Denormalization** – Duplicating data (e.g. username on every post) to speed up reads; the cost
  is updating every copy on change.
  - **When it's OK** – Analytics on rarely changing data, event logs and audit trails (point-in-time
    snapshots), and read-optimized systems like search.
  - **Better option** – Keep the normalized DB as the *source of truth* and put a denormalized cache
    (precomputed joins or aggregates) in front.

- **Sharding in Data Modeling** – Full notes are in the Database Sharding section.
  - **Shard by the primary access pattern** – e.g. shard posts by `user_id` when the main query is
    "posts by user".
  - **Shard key is effectively permanent** – It affects every query and is very hard to change
    later.

- **Interview Steps When Adding a DB** – (1) pick the DB type, (2) list columns per entity, (3)
  define PKs and FKs, (4) choose indexes and say why, (5) decide whether to denormalize, (6) decide
  whether to shard and choose the key.

## API Design

### API Types

- **REST** – HTTP methods acting on URL-identified *resources*. *Interview tip:* The default; say so
  quickly and move on.
- **GraphQL** – A single endpoint where the client's query asks for exactly the fields it needs,
  using a typed schema. Pick it when the problem hints at *over-fetching* or *under-fetching* (e.g.
  mobile and web needing different data).
- **RPC (gRPC)** – Action-style calls that look like local function calls, using *Protocol Buffers*
  (binary) over HTTP/2. Fast, type-safe, and supports streaming. Best for internal
  service-to-service traffic, while REST faces public clients.
- **Real-time (WebSockets, SSE)** – Persistent connections rather than request/response; covered in
  real-time updates.

### REST

- **Resource Modeling** – Use plural nouns, not actions (`/events`, `/bookings`); nest for required
  parent-child relationships (`/events/{id}/tickets`).
- **Path vs Query Params vs Body** – *Path* identifies a required resource (`/events/123`); *query*
  holds optional filtering, sorting and pagination (`?section=VIP`); *body* carries the create or
  update payload.
- **HTTP Methods** – `GET` reads with no side effects; `POST` creates and is *not idempotent*; `PUT`
  replaces a whole resource and is idempotent; `PATCH` partially updates and is idempotent only if
  implemented that way; `DELETE` removes and is idempotent.
- **Idempotency** – Repeating the same request leaves the server in the same state, which makes
  retries safe.
- **Status Codes** – 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found, 429 Too
  Many Requests, 500 Server Error. *Interview tip:* What matters is 4xx = client's fault vs 5xx =
  server's fault.

### GraphQL Terms

- **Over-fetching** – A fixed endpoint returns more data than the client needs.
- **Under-fetching** – The client must make several calls to gather related data.
- **N+1 Problem** – Resolving a related field per item fires one query per item; fix with batching
  (the *DataLoader* pattern).
- **Field-Level Authorization** – Permissions can be checked per field, not just per endpoint.

- **API Design Principles** – Stateless (any server can handle any request), consistent naming and
  response shapes, *principle of least surprise* (use HTTP as expected), secure by default,
  *backward compatible* (adding fields is safe, renaming or removing is not), and actionable errors.

### Pagination

- **Offset-Based** – `?offset=40&limit=20`; simple, but pages shift or duplicate when data changes
  underneath.
- **Cursor-Based** – Server returns a `next_cursor` pointing at the last record; stable for live,
  growing data, but can't jump to an arbitrary page.
- *Interview tip:* Mention pagination for every list endpoint and cap the page size.

- **Idempotency Key** – The client sends a unique key with a create request; on a retry with the
  same key, the server returns the stored result instead of acting again. *Interview tip:* Always
  bring it up for payments, bookings and orders.

- **Versioning** – *URL versioning* (`/v1/events`) is the common, interview-friendly choice; *header
  versioning* keeps URLs clean but is less visible.

### Security

- **Authentication (AuthN)** – Verifies *who* the caller is.
- **Authorization (AuthZ)** – Verifies whether that caller *may* do this action on this resource.
- **API Key** – A long random string identifying an *application*; for server-to-server and
  third-party developers, not end users.
- **JWT (JSON Web Token)** – A signed token carrying user claims and an expiry; verified
  *statelessly*, so it suits distributed systems and user sessions.
- **RBAC (Role-Based Access Control)** – Permissions go to roles (customer, admin) and roles go to
  users.
- **Rate Limiting / Throttling** – Caps requests per user, IP or endpoint at the gateway or
  middleware, returning 429 when exceeded.

- *Interview tip:* Spend at most about 5 minutes on APIs. Default to REST, mark which endpoints need
  auth and which roles, and add pagination and idempotency keys where relevant.

## Object Storage (Blob Storage)

- **Object Storage** – A service built to store large, unstructured files (*blobs*: images, videos,
  documents) cheaply and durably, addressed by a key/URL (Amazon S3, Google Cloud Storage, Azure
  Blob).
  - **Why it exists** – Storing large binaries in a DB is costly and hurts query performance,
    backups and replication; blob storage is far cheaper. *Example:* S3 at ~$0.023/GB/month vs
    DynamoDB at ~$1.25/GB/month.
  - **Rule of thumb** – Files over ~10 MB that don't need SQL queries belong in blob storage.

### How It Works

- **Bucket + Key** – Objects live in a *flat namespace*: a bucket holds objects identified by a
  unique key; "folders" are just key prefixes (`videos/123.mp4`).
- **Immutable Objects** – You can't edit an object in place; a change means uploading a new version
  that overwrites it.
- **Metadata Service + Storage Nodes** – A metadata layer maps each key to where its bytes live, and
  storage nodes hold the actual data.
- **Durability** – Data is protected via *replication* and *erasure coding* across disks and
  machines (S3 advertises 11 nines, 99.999999999%).
- **Scalability** – Effectively unlimited storage and request capacity; treat it as a given in
  interviews.

- **Blob + Metadata DB Pattern** – Store the bytes in blob storage and a *pointer* (key/URL) plus
  metadata in your DB (Postgres, DynamoDB); the DB handles querying and indexing. *Examples:*
  YouTube, Instagram, Dropbox.

- **Presigned URL** – A temporary, signed URL that grants upload or download access to one specific
  object, so clients transfer bytes *directly* to or from blob storage instead of through your app
  servers.
  - **Upload Flow** – (1) client asks the server to upload, (2) server returns a presigned URL and
    saves a pending record in the DB, (3) client uploads directly to blob storage, (4) blob storage
    notifies the server on completion, and the server marks the record done.
  - **Download Flow** – Server returns a presigned URL, and the client downloads via a CDN that
    fetches from blob storage (the *origin*).
  - **Anti-pattern** – Routing file bytes through app servers (server as proxy) wastes bandwidth and
    breaks down for large files.

- **Multipart (Chunked) Upload** – Split a large file into chunks uploaded separately, enabling
  *parallel* and *resumable* uploads (a failed chunk is retried alone); supported natively by S3.

- **CDN in Front** – Blob storage acts as the *origin*, and the CDN caches objects at edge locations
  for fast global downloads.

- **Other Features** – Versioning, *lifecycle policies* (auto-move to cheaper tiers or delete after
  N days), access control, and encryption at rest and in transit.

- *Interview tip:* Never use blob storage as your primary DB. Say "blob in S3, metadata in the DB",
  explain the presigned URL upload/download flow, add a CDN for downloads and multipart for large
  files. Default to S3; it's widely understood and many providers offer S3-compatible APIs.

---

## Review items (separate from source notes)

The PDF-derived notes above are preserved as provided. These points are candidates for a separate accuracy review; they have not been edited in the notes.

- Queue delivery guarantees, end-to-end “exactly-once,” and the general persistence/replication description depend on the queue implementation and its configuration.
- The SQS Standard/FIFO summary and cache-library implementation examples may depend on current service behavior or library versions.
- The Postgres capacity figures and S3-versus-DynamoDB storage prices are workload- and date-sensitive estimates; verify before relying on them.
- Statements such as “almost never the right answer,” “default choice,” and the Facebook/MySQL graph example are interview heuristics or broad claims that may need context.

## Continuation workflow

Use this file as the master revision sheet for future system-design concepts. Add dictated concepts under the existing section structure, preserve the concise pointer style, and keep proposed technical corrections in the review-items section until approved.
