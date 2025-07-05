# Cache System

A custom-built, thread-safe, segment-based **caching system** implemented in Java using **Netty for networking** and a custom **LRU eviction policy**.

---

## Features

### Phase 1: In-Memory Cache (Complete)
- [x] Key-Value storage with `GET`, `PUT`, `DELETE`
- [x] Segmented architecture for improved concurrency
- [x] Thread-safe LRU eviction policy
- [x] Metrics and logging for debugging

### Phase 2: Networking & Protocol (In Progress)
- [x] Custom binary protocol over TCP
- [x] Netty-based server/client architecture
- [x] Encryption support (AES-based transport security)
- [ ] Load balancer stub

### Phase 3: Distribution (Upcoming)
- [ ] Consistent hashing across nodes
- [ ] Data replication and fault tolerance
- [ ] Cluster membership and dynamic joins
- [ ] Rebalancing strategies

---

## Dependencies

- **Java 17**
- **Netty** (TCP server/client)
- **SLF4J + Logback** (logging)
- **Lombok** (boilerplate reduction)

---
