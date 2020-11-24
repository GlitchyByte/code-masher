# Java coalescer for CodinGame competitions

The Glitchy CodinGame Coalescer, or GCC for short, is a tool
that allows you to code in Java, using multiple class files like
a normal app would, when participating in
[CodinGame](https://www.codingame.com/) challenges or
competitions.

---
#### Run it!

You need at least Java 15.

If you download the release, you can simply run it like this:

    java -jar gcc.jar WATCHED_DIR OUTPUT_DIR

If you clone the repo:

    ./run WATCHED_DIR OUTPUT_DIR

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
*Player.java*. CodinGame mandates that `main` is in a class named
`Player`, thus the final file is *Player.java*. You should also
have a *Player.java* in your code as the main file.

Example minimal *Player.java*:

```java
class Player implements Runnable {

    public static void main(final String[] args) {
        final Player player = new Player();
        player.run();
    }

    // [[GCC::CODE]]

    @Override
    public void run() {
        // TODO: Your code here.
    }
}
```

Note the comment with `[[GCC::CODE]]` in it. **This is a magic
tag, and it must exist!** That is the point where all your other
classes are injected. Your classes will automatically be made
`static`, and all imports will be correctly added, with no
duplicates, to the top of the generated file.
