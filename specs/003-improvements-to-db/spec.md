# Feature Specification: Database Improvements for Querying and Data Integrity

**Feature Branch**: `003-improvements-to-db`  
**Created**: 2025-10-16  
**Status**: Draft  
**Input**: User description: "003-improvements-to-db' where you plan and document the issue we are trying to fix (as per your last output)"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Add and List Transactions (Priority: P1)

As a user, I want to add transactions with person, amount, date, and an optional note, and then be able to list all transactions, so I can keep track of my financial activities.

**Why this priority**: This is the core functionality of the ledger application. Without it, no other features are meaningful.

**Independent Test**: Can be fully tested by adding several transactions and then listing them all, verifying that all details are present and correct.

**Acceptance Scenarios**:

1.  **Given** the database is initialized, **When** I add a transaction with a person, amount, date, and note, **Then** the transaction is successfully recorded and appears in the list of all transactions.
2.  **Given** the database is initialized, **When** I add multiple transactions for different persons and dates, **Then** all transactions are listed correctly when I request all transactions.

---

### User Story 2 - Filter Transactions by Person and Date (Priority: P1)

As a user, I want to list transactions filtered by a specific person or by a date range, so I can easily find relevant transactions.

**Why this priority**: This directly addresses the current issue with `JSON_EXTRACT` and is essential for practical use of the ledger.

**Independent Test**: Can be fully tested by adding transactions for multiple persons and dates, then listing transactions for a specific person, and listing transactions since a specific date, verifying the accuracy of the filtered results.

**Acceptance Scenarios**:

1.  **Given** transactions exist for multiple persons, **When** I list transactions for a specific person, **Then** only transactions belonging to that person are displayed.
2.  **Given** transactions exist across various dates, **When** I list transactions since a specific date, **Then** only transactions from that date onwards are displayed.
3.  **Given** transactions exist, **When** I list transactions with a specified limit, **Then** only the specified number of latest transactions are displayed.

---

### User Story 3 - View Balances (Priority: P1)

As a user, I want to view the current balance for all persons or for a specific person, so I can understand their financial standing.

**Why this priority**: This is a critical feature for financial tracking and directly impacted by the data integrity issues.

**Independent Test**: Can be fully tested by adding a series of positive and negative transactions for different persons, then checking the overall balances and individual balances, verifying their accuracy.

**Acceptance Scenarios**:

1.  **Given** transactions exist for multiple persons, **When** I request all balances, **Then** a correct summary of each person's balance is displayed.
2.  **Given** transactions exist for a specific person, **When** I request the balance for that person, **Then** their correct total balance is displayed.

---

### User Story 4 - Backup and Restore Database (Priority: P2)

As a user, I want to be able to back up my ledger database and restore it, so I can ensure data safety and portability.

**Why this priority**: Data safety is important, but the core functionality of adding/listing/balancing takes precedence.

**Independent Test**: Can be fully tested by adding transactions, backing up the database, restoring it to a new location, and then verifying the contents of the restored database.

**Acceptance Scenarios**:

1.  **Given** a populated database, **When** I initiate a backup, **Then** a backup file is created successfully.
2.  **Given** a valid backup file, **When** I restore the database to a new path, **Then** the restored database contains all the original transactions and balances.

### Edge Cases

-   What happens when adding a transaction with an empty person or date? (Should return an error)
-   How does the system handle listing transactions or balances when the database is empty? (Should return an empty list/zero balance)
-   What happens if an invalid date format is provided for filtering? (Should return an error)

## Requirements *(mandatory)*

### Functional Requirements

-   **FR-001**: The system MUST store each transaction with a unique identifier, person, amount, date, and an optional note.
-   **FR-002**: The system MUST store the `person` and `date` of each transaction in dedicated, queryable columns within the `transactions_history` table.
-   **FR-003**: The system MUST allow retrieval of all transactions.
-   **FR-004**: The system MUST allow filtering of transactions by `person` using the dedicated `person` column.
-   **FR-005**: The system MUST allow filtering of transactions by `date` (specifically, transactions `since` a given date) using the dedicated `date` column.
-   **FR-006**: The system MUST allow limiting the number of transactions returned.
-   **FR-007**: The system MUST calculate and display the aggregate balance for all persons.
-   **FR-008**: The system MUST calculate and display the balance for a specific person.
-   **FR-009**: The system MUST provide functionality to back up the database.
-   **FR-010**: The system MUST provide functionality to restore the database from a backup.
-   **FR-011**: The system MUST ensure data consistency between the dedicated `person` and `date` columns and the `data` JSON blob for each transaction.

### Key Entities *(include if feature involves data)*

-   **Transaction**: Represents a single financial movement. Key attributes include `person` (who the transaction is for), `amount` (the value of the transaction), `date` (when the transaction occurred), and `note` (optional description).
-   **Person**: Represents an individual or entity associated with transactions. Key attribute is `name`.
-   **Balance**: Represents the aggregated financial total for a specific `person`. Key attributes are `person` and `balance` (total amount).

## Success Criteria *(mandatory)*

### Measurable Outcomes

-   **SC-001**: All `list` operations (all, by person, by date, with limit) return 100% accurate transaction data.
-   **SC-002**: All `balance` and `balances` operations return 100% accurate financial totals.
-   **SC-003**: `list` operations filtered by `person` or `date` complete in under 100ms for a database containing 10,000 transactions.
- **SC-004**: Database backup and restore operations complete successfully without data loss, meaning all original data bits are identical post-restore.
-   **SC-005**: No runtime errors related to missing database functions occur during the execution of `list` or `balance` commands.