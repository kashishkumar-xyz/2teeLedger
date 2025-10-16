# Feature Specification: Core Rust Library Development

**Feature Branch**: `001-phase-1-core`  
**Created**: 2025-10-14  
**Status**: In Progress  
**Input**: User description: "## Phase 1: Core Rust Library Development
1.  **Initialize Rust Project**: Set up a new Rust library project (`cdylib`).
2.  **Define Core Data Structures**: Implement the `Transaction` struct and logic for managing a list of transactions.
3.  **Implement Ledger Logic**: Develop functions for adding transactions, computing balances (`get_balance`, `list_balances`), and listing transactions (`list_transactions`).
4.  **Integrate `rusqlite`**: Set up basic SQLite database interactions using `rusqlite` for CRUD operations.
5.  **FFI/JNI Interface**: Expose core Rust functions via a C ABI or JNI interface using the `jni` crate, ensuring safe memory ownership.
6.  **Unit Testing**: Write comprehensive unit tests for all core ledger logic and database interactions."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Record a new transaction (Priority: P1)

As a user, I want to record a new financial transaction with details like who is involved, the amount, the date, and an optional note, so I can keep track of what people owe me or what I owe them.

**Why this priority**: This is the fundamental action of a ledger application.

**Independent Test**: A user can successfully add a transaction and see it reflected in the transaction list.

**Acceptance Scenarios**:

1.  **Given** the ledger is initialized, **When** I add a transaction with a person, amount, date, and note, **Then** the transaction is successfully recorded and appears in the list of transactions.
2.  **Given** the ledger is initialized, **When** I add a transaction with only a person, amount, and date, **Then** the transaction is successfully recorded with an empty note.
3.  **Given** the ledger is initialized, **When** I attempt to add a transaction with invalid data (e.g., empty person, invalid amount), **Then** the transaction is rejected, and an error message is provided.

**Constitution Alignment**: Aligns with "Append-Only Ledger" (transactions are added), "Perspective-Based Transactions" (amount reflects owe/due), and "Security-First Design" (input validation).

---

### User Story 2 - View all transactions (Priority: P1)

As a user, I want to view a list of all recorded transactions, so I can review my financial history.

**Why this priority**: Essential for reviewing and verifying ledger entries.

**Independent Test**: A user can open the application and see a list of all previously recorded transactions.

**Acceptance Scenarios**:

1.  **Given** multiple transactions have been recorded, **When** I request to view all transactions, **Then** a complete list of all transactions is displayed in chronological order.

**Constitution Alignment**: Aligns with "Append-Only Ledger" (viewing history).

---

### User Story 3 - View balances for all people (Priority: P1)

As a user, I want to see a summary of how much each person owes me or I owe them, so I can quickly understand my financial standing with everyone.

**Why this priority**: Provides a quick overview of financial relationships.

**Independent Test**: A user can see a list of all people with their current aggregated balances.

**Acceptance Scenarios**:

1.  **Given** multiple transactions involving different people have been recorded, **When** I request to view all balances, **Then** a list of each person and their current net balance is displayed.

**Constitution Alignment**: Aligns with "Perspective-Based Transactions" (balances reflect owe/due).

---

### User Story 4 - View individual person's balance (Priority: P2)

As a user, I want to view the total amount a specific person owes me or I owe them, so I can focus on one financial relationship.

**Why this priority**: Useful for managing specific financial relationships.

**Independent Test**: A user can select a person and see their individual balance.

**Acceptance Scenarios**:

1.  **Given** transactions exist for a specific person, **When** I request the balance for that person, **Then** the correct net balance for that person is displayed.
2.  **Given** no transactions exist for a specific person, **When** I request the balance for that person, **Then** a zero balance is displayed.

**Constitution Alignment**: Aligns with "Perspective-Based Transactions".

---

### Edge Cases

- If the database file is corrupted or inaccessible, the system MUST attempt automated recovery from the latest valid local backup. If recovery fails, the user MUST be notified and offered a revert to the last known healthy state.
- How does the system handle extremely large numbers of transactions (performance)?
- **FR-013**: The system MUST gracefully handle incorrect encryption keys during database opening by rejecting the operation and providing a clear error message to the user.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST allow users to add new transactions with a date, person, amount, and an optional note.
- **FR-002**: The system MUST store transactions in an append-only manner.
- **FR-003**: The system MUST calculate and display the net balance for each person based on recorded transactions.
- **FR-004**: The system MUST calculate and display the net balance for a specific person.
- **FR-005**: The system MUST display a list of all recorded transactions.
- **FR-006**: The system MUST validate transaction input data (e.g., non-empty person, valid amount).
- **FR-007**: The system MUST persist all transaction data using an encrypted SQLite database, with the encryption key managed by the Android Keystore (hardware-backed).
- [x] **FR-008**: The system MUST provide an interface for mobile UI integration (FFI/JNI).
- **FR-009**: The system MUST include comprehensive unit tests for core logic and database interactions.
- **FR-010**: The system MUST implement automated recovery for corrupted or inaccessible database files and notify the user if recovery fails, with an option to revert to a last healthy state (e.g., via VCS).
- **FR-011**: The system MUST provide a command-line interface (CLI) for interacting with core ledger functionalities (add, list, balance, backup, restore).
- **FR-012**: The CLI MUST provide clear usage instructions and error messages for invalid commands or arguments.
- **FR-013**: The system MUST gracefully handle incorrect encryption keys during database opening by rejecting the operation and providing a clear error message to the user.
- **FR-014**: The system MUST use HMAC-SHA256 for checksums to verify the integrity of backup files.
- **FR-015**: The CLI MUST support argument parsing using the `clap` library.

### Key Entities *(include if feature involves data)*

- **Transaction**: Represents a single financial event. Attributes: `id` (unique identifier), `date` (ISO 8601), `person` (involved party), `amount` (integer, positive for owed to user, negative for owed by user), `note` (optional description).
- **Balance**: Represents the aggregated financial standing with a person. Attributes: `person`, `total_amount` (integer, net balance).

## Clarifications

### Session 2025-10-14

- Q: How will the encryption key for the SQLite database be managed on Android? → A: Android Keystore (hardware-backed)
- Q: How should the system respond to a corrupted or inaccessible database file? → A: use VCS; try "option B", incase of failed recovery let user know and revert back to last healthy state according to VCS

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can successfully record a new transaction in under 2 seconds.
- **SC-002**: The system accurately calculates and displays all balances within 3 seconds for up to 10,000 transactions.
- **SC-003**: All core ledger functions (add, list, get balance) are covered by unit tests with at least 90% code coverage.
- **SC-004**: The Rust core library successfully integrates with a mobile UI via FFI/JNI, allowing all core functions to be called without crashes or memory leaks.