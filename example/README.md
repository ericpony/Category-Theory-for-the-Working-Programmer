Examples
========

This is a simple example project. You may run the examples using sbt:

    $ sbt # opens an sbt console

    > run
    [info] Running example.one.Main
    Hello, example world!

Or you may decide to open the REPL and play with them directly:

    $ sbt # opens an sbt console

    > console
    [info] Starting scala interpreter...
    [info] 
    Welcome to Scala version 2.10.3 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0).
    Type in expressions to have them evaluated.
    Type :help for more information.

    scala> example.one.Main.main(Array())
    Hello, example world!

IDE integration
---------------
If you want to generate IDE files, use the following sbt commands:

    > eclipse

    > gen-idea

You should then be able to point your IDE to this folder and see an existing project.
If the project changes in meaningful ways you may need to regenerate the IDE files.