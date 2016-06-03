# ![](https://raw.githubusercontent.com/czak/retronix/assets/logo.png)

Retronix is my Java remake of the 1984 classic MS-DOS game, Xonix.
A platform-independent core and three backends allow it to build for the
following platforms:

![](https://raw.githubusercontent.com/czak/retronix/assets/platforms.jpg)

You can [play online now](http://retronix.czak.pl) or download the Android
version [on Google Play](https://play.google.com/store/apps/details?id=pl.czak.retronix.android).

## How to play

## On desktop & the web

* Cursor keys for navigation
* <kbd>Enter</kbd> to select menu items
* <kbd>Esc</kbd> to pause

## On touch-enabled Android devices

* Swipe to change direction
* Tap to select menu items
* <kbd>Back</kbd> to pause

## On Android TV

* D-pad for navigation & selection
* <kbd>Back</kbd> to pause

## How to build

### For desktop

```sh
$ ./gradlew desktop:run
```

### Web

```sh
$ ./gradlew web:gwtDev
```

### Android

```sh
$ ./gradlew android:installDebug
```

## License

Copyright (c) 2016 Łukasz Adamczak

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
