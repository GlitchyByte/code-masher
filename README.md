# Java Coalescer for CodinGame challenges

![Version](https://img.shields.io/badge/Version-3.0.2-blue) ![Java](https://img.shields.io/badge/Java-17-orange)

CodeMasher is a tool that allows you to code in Java using multiple class files, like a normal app would, when working on [CodinGame](https://www.codingame.com/) bot challenges.

It constantly observes a given directory for changes. It automatically and properly coalesces all classes. The tool is also a webserver that only serves the last fully coalesced file that successfully compiled.

On the actual [CodinGame](https://www.codingame.com/) bot IDE page you need to use the [CodeMasherLoader](https://github.com/GlitchyByte/code-masher-loader) (Chrome extension) to update the code automatically.


---
## Run it!

If you want to run from source on any platform, you need at least
[Java 17](https://jdk.java.net).

    run/run codemasher -w WATCHED_DIR

**WATCHED_DIR** Directory where your source code resides. For example, the `src` directory in a project generated by your IDE.

Use `-h` to see other options.

---
## Example

Example minimal *Player.java*:

```java
class Player implements Runnable {
    // [[CM::UPDATESTAMP]]

    public static void main(final String[] args) {
        final Player player = new Player();
        player.run();
    }

    // [[CM::CODE]]

    @Override
    public void run() {
        // TODO: Your code here.
    }
}
```

Note the two `CM` comments:

* `[[CM::UPDATESTAMP]]` Gets replaced by the time at the moment of
coalescing and an ASCII graphic that makes it evident that the file
has changed.

* `[[CM::CODE]]` **This must exist!** This is the point where all
your other classes are injected. Your classes will automatically be
made `static`, and all imports will be correctly added, with no
duplicates, at the top of the generated file.
