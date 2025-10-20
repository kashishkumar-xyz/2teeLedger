## Screen 1

- The screen is divided into 5 distinct rows (in the given order)
  1. screen title
  2. user-input render display
  3. keypad
  4. operations bar 
  5. navbar

* use of state and mode has been done interchangibly in this document, both imply the same underlying concept, but change depending on the object its being used with. mode usually describes the apps context, while state describes the visual appearence the app takes on depending on the mode

### keypad layout
- 12 total buttons in the below order

```
[1] [2] [3]
[4] [5] [6]
[7] [8] [9]
[_] [0] [󰭜]
```

. [_] : clear all
. 󰭜 : backpace

### operations bar
- has 3 buttons 
  1. negative operation mode
  2. sumbit amount
  3. positive operation mode

```
|[-] [>] [+]|
```

- the operation bar can be in 2 distinct states which would dictate its visual apperance. 
      - green state (*default)    : when in positive mode
      - red state                 :  when in negative mode
  - the state operation bar is in depends on the *operation-mode* app is in
- the submit button takes the value user has entered and prepends it with the approprite symbol ( + / - ), again depending on the state and mode the app was in just before user presses submit
- additionally depending on the state/mode app is in the 3 buttons in operations bar will have a superficial (only for visual purposes) grouping between submit button and the corresponding mode button, below is text representation of the intent that i am trying to describe

```
 ........
|:[-] [>]: [+]|   = negative mode
 ˙˙˙˙˙˙˙˙

     .........
|[-] :[>] [+]:|   = positive mode
     ˙˙˙˙˙˙˙˙˙
```

- also depending the mode the app is in the app decides what foreground color it will use for 4 (out of the total 5) portions of its app, the portions affected by this foreground color are: screen title, user-input display, keypad, operations bar
  - default color: green --> positive mode
  - red --> negative mode

### navbar
- 3 buttons each when clicked navigates to its corresponding Screen
      1. recents 󰋚
      2. keypad (default)
      3. accounts 󰖸

#### additional notes for the UI designer
- the textual representations provided in this document are only for your conceptual understanding. pls dont use them literally in your designs, i dont want [] (brackets)  or ... as they are presented here!

- the app will be dark themed, choose a color and shade pallette based on this, make sure the shade of green and red you choose are in  a good contrast ratio with the dark background

- the navbar shouldnt be affected by the state or mode 
