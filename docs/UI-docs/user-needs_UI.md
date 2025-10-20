
- display a 'sorted' list of all 'non-zero' balances

- a 'fzf' enabled 'text-field', where user can type.
  - should display A 'scrollable' list based on the fzf logic
  - if no search match found then <create new?> confirmation prompt to be displayed

- fzf -> full text search, not exactly fuzzy finding as that gives too much room for ambiguity.
[search types: article](https://zilliz.com/blog/semantic-search-vs-lexical-search-vs-full-text-search#Full-Text-Search)

---

UI - layout: android phone (portrait mode)

i want a plain screen with just one input field that only takes in numbers as inputs (format: currency/monetary value, prefix: $)

should only allow for whole numbers (no decimals). the text should be large and the only focused item on the screen.

inspiration: Android's Phone app in 'Keypad' mode

the keypad area should be swipeable with the following modes:

- Num# :Positive (default)  *Green backdrop
- Num# :Negative  *Red backdrop
- Str@ :Name Search

### flow of modes and their swipe logic

```
                                   ╭── right swipe -> @str: name_search
                                   │
                             [#num: positive+]
                                   │
[#num: negative-] <- left swipe  ──╯
```
