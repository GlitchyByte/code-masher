# Java coalescer for CodinGame challenges

The GlitchyByte CodinGame Coalescer, or GBCC for short, is a tool
that allows you to code in Java, using multiple class files like
a normal app would, when participating in
[CodinGame](https://www.codingame.com/) challenges or
competitions.

---
#### Run it!

This release includes a macOS binary, so you don't need anything
else if you are on a Mac.

If you want to run from source on any platform, you need at least
Java 16.

After cloning the repo:

    ./run gbcc WATCHED_DIR OUTPUT_DIR

Where **WATCHED_DIR** is the directory where your source code
resides. For example, the `src` directory in a project generated
by your IDE. And **OUTPUT_DIR** is where the final *Player.java*
will be written. This is the file you want to link in the
[CodinGame Chrome plugin](https://www.codingame.com/forum/t/codingame-sync-beta/614).

Note that `./run` will also compile the 1st time. So don't worry
about anything else, if all you want to do is use it.

---
#### What does it do?

It constantly observes WATCHED_DIR for changes, and it
automatically coalesces all files/classes into the main
*Player.java* file. CodinGame mandates that `main` is in a class
named `Player`, thus the final file is *Player.java*. You should
also have a *Player.java* in your code as the main file.

Example minimal *Player.java*:

```java
class Player implements Runnable {
    // [[GBCC::UPDATESTAMP]]

    public static void main(final String[] args) {
        final Player player = new Player();
        player.run();
    }

    // [[GBCC::CODE]]

    @Override
    public void run() {
        // TODO: Your code here.
    }
}
```

Note the two GBCC comments:

* `[[GCC::UPDATESTAMP]]` Gets replaced by the time at the moment of
coalescing and an ASCII graphic that makes it evident that the file
has changed.

* `[[GCC::CODE]]` **This must exist!** This is the point where all
your other classes are injected. Your classes will automatically be
made `static`, and all imports will be correctly added, with no
duplicates, at the top of the generated file.
