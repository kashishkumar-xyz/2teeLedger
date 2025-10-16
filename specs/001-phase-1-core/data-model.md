# Data Model: Core Rust Library Development

## Entities

### Transaction

Represents a single financial event.

**Attributes:**

- `id`: Unique identifier (integer, auto-incremented by DB).
- `date`: Date of the transaction (string, ISO 8601 format: YYYY-MM-DD).
- `person`: Name of the person involved (string, non-empty, sanitized).
- `amount`: Amount of the transaction (integer, in cents; positive if person owes user, negative if user owes person).
- `note`: Optional description of the transaction (string).

**Relationships:**

- None (standalone entity).

**Validation Rules:**

- `date`: Must be a valid date string in YYYY-MM-DD format.
- `person`: Must be a non-empty string. Input should be sanitized.
- `amount`: Must be an integer.
- `note`: Optional.

### Balance

Represents the aggregated financial standing with a person. This is a derived entity, computed from `Transaction` records.

**Attributes:**

- `person`: Name of the person (string).
- `total_amount`: Net balance for the person (integer, positive if person owes user, negative if user owes person).

**Relationships:**

- Derived from `Transaction` entities.

**Validation Rules:**

- None (derived entity, consistency depends on `Transaction` validation).

### Transaction History (Append-Only Versioned Table)

Represents the logical history of `Transaction` entities for fine-grained rollback and auditing.

**Attributes:**

- `tx_id`: Domain ID of the transaction (TEXT, stable across versions).
- `version`: Monotonic version number for each `tx_id` (INTEGER).
- `data`: JSON payload of the transaction at this version (JSON).
- `op`: Type of operation ('insert', 'update', 'delete') (TEXT).
- `created_at`: Timestamp of the record creation (TIMESTAMP, default CURRENT_TIMESTAMP).

**Relationships:**

- Tracks historical states of `Transaction` entities.

**Validation Rules:**

- `tx_id`, `version` form a primary key.
- `op` must be one of 'insert', 'update', 'delete'.