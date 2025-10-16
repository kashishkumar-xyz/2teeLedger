# Research: Database Improvements for Querying and Data Integrity

## Decision: Store `person` and `date` as separate columns in `transactions_history`

### Rationale:

The `sqlcipher` library in the current development environment (Docker/Linux) does not support the SQLite JSON1 extension, which provides the `JSON_EXTRACT` function. This unavailability prevents correct filtering and ordering of transactions by `person` and `date` when these fields are only stored within a JSON blob.

Storing `person` and `date` in dedicated, indexed columns directly addresses this functional limitation. It enables efficient and correct querying, filtering, and ordering of transactions, which is critical for the `list` and `balance` commands.

### Alternatives Considered:

1.  **Building `sqlcipher` from source with JSON1 enabled:** This alternative was rejected due to its significant complexity, the increased Docker build time it would introduce, and the user's preference to avoid complex Docker setups for development/testing, especially given that the ultimate deployment target is Android/GrapheneOS, where `sqlcipher` integration will be handled differently.
2.  **Ignoring the issue:** This was rejected as it directly breaks core functionality (transaction filtering, balance calculations) and leads to an unusable application for its primary purpose.
