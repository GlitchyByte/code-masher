# Base Java App with Gradle Kotlin Build

## Run the app!

Sure, you can do `./gradlew run`, but the app won't have a console,
or respond correctly to `SIGINT` (`Ctrl+C`). Gradle will immediately
kill your app without giving it a chance to exit nicely.
So, I've included a nifty `run` script that handles building with
Gradle, then running the app from the distribution output.

    ./run

You can't run a debugging session though. I haven't figured that part
out yet.

## The included library

- Logging facility with one line logs and thread indicators.
- General String, json, and other helpers.
- Console color printing.
- BuildInfo singleton that works with Gradle produced output
(read below).

## The build

Build will trigger an info file `build-info.json` to be
generated. Contents will be like this:

    {
      "group": "com.glitchybyte.example" // Project group.
      "version": "1.0",                  // Basic version.
      "date": "20201025",                // Build date.
      "code": "46mbx9j"                  // Unique build code.
    }

`code` will be a short and easily usable build identifier (e.g.,
build version, image tag, etc.). It will be unique for the
particular project once per second until around the year 2150.
No actual words will be formed.

This file will be automatically generated in the
`src/main/resources` directory (configurable), and it can also be
saved in another or many other directories. This way, it can be
used by many other scripts or programs, and they will have the
same code available.

##### Uber jar?
There is commented code in `build.gradle.kts` for creating an uber
jar, if needed.
