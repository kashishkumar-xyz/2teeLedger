# Data Model: Database Improvements for Querying and Data Integrity

## Entities

### Transaction
Represents a single financial movement, stored in the `transactions_history` table.

**Attributes (Database Columns)**:
- `tx_id`: TEXT (UUID, part of PRIMARY KEY)
- `version`: INTEGER (part of PRIMARY KEY)
- `person`: TEXT (New dedicated column for filtering and querying)
- `date`: TEXT (YYYY-MM-DD format, New dedicated column for filtering and querying)
- `data`: TEXT (JSON string containing the full transaction details: `id`, `date`, `person`, `amount`, `note`)
- `op`: TEXT (Operation type, e.g., "insert", "delete")
- `created_at`: TEXT (ISO 8601 timestamp)

**Relationships**:
- None directly modeled within the database schema, but `person` implicitly relates to the `Person` concept.

### Person
Represents an individual or entity associated with transactions. Not a separate database table, but a conceptual entity derived from the `person` column in `transactions_history`.

**Attributes**:
- `name`: String (Corresponds to the `person` column in `transactions_history`)

### Balance
Represents the aggregated financial total for a specific person. This is a derived entity, calculated from `Transaction` records.

**Attributes**:
- `person`: String
- `balance`: i64 (Sum of `amount` for a given `person`)
