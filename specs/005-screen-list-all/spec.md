# Feature Specification: Search Screen for Account Balances

**Feature Branch**: `005-screen-list-all`  
**Created**: October 21, 2025  
**Status**: Draft  
**Input**: User description: "screen: list_all_balances **1. Project Objective** Design a clean, modern, and intuitive "Search Screen" for a financial application. The primary function of this screen is to allow users to quickly find and view account balances by searching for the account holder's name. The application's main purpose is to display a list of account balances in Australian Dollars (AUD). The design must be based on a dark mode theme and should feel integrated with the native mobile OS. **2. Screen Layout & Key Elements** The screen should be composed of three main sections from top to bottom: the search bar, the search results, and the native system keyboard. * **Search Input Field:** * **Position:** Fixed at the top of the screen, with standard margin/padding from the edges. * **Content:** Should contain a search (magnifying glass) icon on the left and a text input area. The user's typed text (e.g., "Jo") should be visible. * **Styling:** A rounded rectangle shape with a background color slightly lighter than the main screen background. * **Suggestions/Results Area:** * **Section Title:** A text label "Suggestions" should be placed below the search bar on the left. * **"New Contact" Button:** On the same horizontal line as the "Suggestions" title, but aligned to the far right, there should be a `+ New Contact` text button. The plus symbol should be an icon. * **Account List:** A vertically scrolling list of accounts that match the search query. Each list item represents a user's account and must contain: * **Avatar:** A circular profile picture of the account holder on the left. * **Account Holder Name:** The full name of the person (e.g., "Avery Johnson") displayed prominently next to the avatar. * **Account Balance:** The account's current balance in AUD (e.g., "$240") displayed directly below the name. The color of this text is critical: it must be **green for positive balances (credit)** and **red for negative balances (debit/overdrawn)**. * **Keyboard:** * **Type:** The native Android keyboard. * **State:** The keyboard must be visible and active by default when the user lands on this screen. * **Theme:** It should use its corresponding dark theme to match the app's UI. The primary action button (e.g., "Enter" or "Return") should be styled as a "Go" button. **3. Color Palette** The design should adhere to a dark mode aesthetic. Use the following color scheme as a guide: * **Main Background:** `#181A15` * **Search Bar Background:** `#2C2F2A` (A slightly lighter shade to create depth) * **Primary Text & Icons (Names, Titles, '+'):** `#EAEAEA` (Slightly off-white for better readability) * **Placeholder Text (in search bar):** `#888888` (A muted gray) * **Positive Balance Accent (Credit):** `#4CAF50` (A clear, vibrant green for balances like "$240") * **Negative Balance Accent (Debit/Overdrawn):** `#F44336` (A clear, vibrant red for balances like `-$50`) * **Keyboard Action Button ('Go'):** `#3B82F6` (A standard system blue to indicate a primary action) **4. Typography** * **Font Family:** Use a clean, sans-serif font like **Roboto** or **Inter** for a modern and native feel. * **Font Sizes & Weights:** * **Suggestions Title:** 18pt, Medium weight * **Account Holder Name:** 16pt, Regular weight * **Account Balance:** 14pt, Regular weight * **Search Input Text:** 16pt, Regular weight **5. Reference** Please use the provided image as a strong visual reference for the overall layout, spacing, element styling, and general aesthetic. The goal is to achieve a very similar look and feel while ensuring all components are polished and the financial information (balances) is communicated clearly and instantly through the use of color. refrence pic: @docs/UI-docs/designs/screens/account-search/ui.png"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Search for an existing account by name (Priority: P1)

As a user, I want to search for an account holder by name so that I can quickly find their balance.

**Why this priority**: This is the core functionality of the screen, allowing users to access critical financial information efficiently.

**Independent Test**: This can be fully tested by opening the screen, typing a partial name, and verifying that matching accounts are displayed with correct balance information and styling.

**Acceptance Scenarios**:

1.  **Given** the user is on the Search Screen, **When** the screen loads, **Then** the native Android keyboard is visible and active.
2.  **Given** the user types "Jo" into the search bar, **When** matching accounts exist (e.g., "John Doe", "Jane Johnson"), **Then** a vertically scrolling list of these accounts is displayed below the search bar.
3.  **Given** an account with a positive balance (e.g., "$240"), **When** it is displayed in the list, **Then** the balance text is green (`#4CAF50`).
4.  **Given** an account with a negative balance (e.g., "-$50"), **When** it is displayed in the list, **Then** the balance text is red (`#F44336`).
5.  **Given** the search bar is displayed, **When** the screen is in dark mode, **Then** the search bar background is `#2C2F2A` and primary text/icons are `#EAEAEA`.

---

### User Story 2 - No matching accounts found (Priority: P2)

As a user, when I search for an account that does not exist, I want to be informed that no results were found so that I understand the search outcome.

**Why this priority**: Provides clear and immediate feedback to the user, preventing confusion or frustration.

**Independent Test**: This can be tested by typing a non-existent name into the search bar and verifying that an appropriate "No results found" message is displayed.

**Acceptance Scenarios**:

1.  **Given** the user types a name with no matching accounts, **When** the search completes, **Then** a "No results found" message is displayed in the results area.

---

### User Story 3 - Initiate adding a new contact (Priority: P3)

As a user, I want to easily add a new contact from the search screen so that I can manage my contacts efficiently.

**Why this priority**: Enhances user convenience by providing a direct pathway to contact management from a related screen.

**Independent Test**: This can be tested by tapping the "+ New Contact" button and verifying that the application navigates to the designated contact creation screen.

**Acceptance Scenarios**:

1.  **Given** the user is on the Search Screen, **When** the user taps the "+ New Contact" button, **Then** the application navigates to the "Add New Contact" screen.

---

### Edge Cases

-   **Empty Search Query**: When the search query is empty, the screen should display the "Suggestions" title and the "+ New Contact" button, but no account list.
-   **Backend Service Unavailable**: If the backend service providing account data is unavailable, an informative error message (e.g., "Unable to load accounts. Please try again later.") should be displayed.
-   **Missing Avatar**: If an account has no specified avatar, a default placeholder avatar should be displayed.
-   **Long Account Holder Name**: If an account holder's name is very long, it should be truncated with an ellipsis or wrapped to prevent UI overflow.

## Requirements *(mandatory)*

### Functional Requirements

-   **FR-001**: The system MUST display a search input field fixed at the top of the screen with a search (magnifying glass) icon on the left and a text input area.
-   **FR-002**: The system MUST display the native Android keyboard, active and visible, upon screen load.
-   **FR-003**: The system MUST display a "Suggestions" title aligned left, and a "+ New Contact" button aligned right, below the search bar.
-   **FR-004**: The system MUST display a vertically scrolling list of accounts matching the search query.
-   **FR-005**: Each account list item MUST include a circular avatar, the account holder's full name, and their current balance in AUD.
-   **FR-006**: The account balance text MUST be green (`#4CAF50`) for positive values and red (`#F44336`) for negative values.
-   **FR-007**: The screen MUST adhere to a dark mode theme with the following specified colors:
    *   Main Background: `#181A15`
    *   Search Bar Background: `#2C2F2A`
    *   Primary Text & Icons (Names, Titles, '+'): `#EAEAEA`
    *   Placeholder Text (in search bar): `#888888`
    *   Keyboard Action Button ('Go'): `#3B82F6`
-   **FR-008**: The screen MUST use a clean sans-serif font (Roboto or Inter) with the following specified sizes and weights:
    *   Suggestions Title: 18pt, Medium weight
    *   Account Holder Name: 16pt, Regular weight
    *   Account Balance: 14pt, Regular weight
    *   Search Input Text: 16pt, Regular weight
-   **FR-009**: The system MUST display a "No results found" message when no accounts match the search query.
-   **FR-010**: The system MUST display a default placeholder avatar if an account has no specified avatar.

### Key Entities *(include if feature involves data)*

-   **Account**: Represents a financial account.
    *   `account_holder_name`: String, the full name of the account holder.
    *   `balance`: Decimal, the current balance in AUD.
    *   `avatar_url`: String (optional), URL to the account holder's profile picture.

## Success Criteria *(mandatory)*

### Measurable Outcomes

-   **SC-001**: 95% of search queries return results within 1 second.
-   **SC-002**: The UI design of the Search Screen matches the provided visual reference (`docs/UI-docs/designs/screens/account-search/ui.png`) with at least 90% fidelity in layout, spacing, and styling.
-   **SC-003**: Users can successfully locate a specific account and view its balance within 15 seconds on average.
-   **SC-004**: The color-coding of account balances (green for positive, red for negative) is correctly applied in 100% of displayed accounts.