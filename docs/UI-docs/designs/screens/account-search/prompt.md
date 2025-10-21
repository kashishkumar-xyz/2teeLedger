
**1. Project Objective**

Design a clean, modern, and intuitive "Search Screen" for a financial application. The primary function of this screen is to allow users to quickly find and view account balances by searching for the account holder's name. The application's main purpose is to display a list of account balances in Australian Dollars (AUD). The design must be based on a dark mode theme and should feel integrated with the native mobile OS.

**2. Screen Layout & Key Elements**

The screen should be composed of three main sections from top to bottom: the search bar, the search results, and the native system keyboard.

* **Search Input Field:**
  * **Position:** Fixed at the top of the screen, with standard margin/padding from the edges.
  * **Content:** Should contain a search (magnifying glass) icon on the left and a text input area. The user's typed text (e.g., "Jo") should be visible.
  * **Styling:** A rounded rectangle shape with a background color slightly lighter than the main screen background.

* **Suggestions/Results Area:**
  * **Section Title:** A text label "Suggestions" should be placed below the search bar on the left.
  * **"New Contact" Button:** On the same horizontal line as the "Suggestions" title, but aligned to the far right, there should be a `+ New Contact` text button. The plus symbol should be an icon.
  * **Account List:** A vertically scrolling list of accounts that match the search query. Each list item represents a user's account and must contain:
    * **Avatar:** A circular profile picture of the account holder on the left.
    * **Account Holder Name:** The full name of the person (e.g., "Avery Johnson") displayed prominently next to the avatar.
    * **Account Balance:** The account's current balance in AUD (e.g., "$240") displayed directly below the name. The color of this text is critical: it must be **green for positive balances (credit)** and **red for negative balances (debit/overdrawn)**.

* **Keyboard:**
  * **Type:** The native Android keyboard.
  * **State:** The keyboard must be visible and active by default when the user lands on this screen.
  * **Theme:** It should use its corresponding dark theme to match the app's UI. The primary action button (e.g., "Enter" or "Return") should be styled as a "Go" button.

**3. Color Palette**

The design should adhere to a dark mode aesthetic. Use the following color scheme as a guide:

* **Main Background:** `#181A15`
* **Search Bar Background:** `#2C2F2A` (A slightly lighter shade to create depth)
* **Primary Text & Icons (Names, Titles, '+'):** `#EAEAEA` (Slightly off-white for better readability)
* **Placeholder Text (in search bar):** `#888888` (A muted gray)
* **Positive Balance Accent (Credit):** `#4CAF50` (A clear, vibrant green for balances like `$240`)
* **Negative Balance Accent (Debit/Overdrawn):** `#F44336` (A clear, vibrant red for balances like `-$50`)
* **Keyboard Action Button ('Go'):** `#3B82F6` (A standard system blue to indicate a primary action)

**4. Typography**

* **Font Family:** Use a clean, sans-serif font like **Roboto** or **Inter** for a modern and native feel.
* **Font Sizes & Weights:**
  * **Suggestions Title:** 18pt, Medium weight
  * **Account Holder Name:** 16pt, Regular weight
  * **Account Balance:** 14pt, Regular weight
  * **Search Input Text:** 16pt, Regular weight

**5. Reference**

Please use the provided image as a strong visual reference for the overall layout, spacing, element styling, and general aesthetic. The goal is to achieve a very similar look and feel while ensuring all components are polished and the financial information (balances) is communicated clearly and instantly through the use of color.
