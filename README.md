# reaktor-codecamp-clj

Reaktor Codecamp Clojure base.

# Prerequisites

Download and install both JVM 1.8 and [Leiningen](http://leiningen.org/).

# Development

## Running The Application

During development you should run the application from a REPL:

```bash
# Use your favorite IDE or invoke this to start the REPL:
$ lein repl
```

Do your thing, reload your code changes and (re)start the system from the REPL by invoking:

```clojure
(reset)
```

You can stop the system by invoking

```clojure
(stop)
```

## The Beef

Code your logic to the `reaktor-codecamp-2016-ii-clj/on-server-event` function. It will be invoked whenever a server sends a message.

The function will receive the JSON payload from server, parsed as Clojure data structure, as an argument. The function must return a similar data structure that will sent back to the server as a response.

# Configuring The Server

In development mode, when running the system from REPL, just open the `project.clj`, find the `:profiles {:dev {:env ... }}` part, set the desired listen port for the UDP server and other values there.

All values set to `project.clj` must be lower-cased and snake-cased as opposed to environment variables that are normally upper-cased and snake-cased.

When running the system either from command line or from a Docker container, just set all the configurations as environment variables. As opposed to `project.clj` based configuration, actual environment variables must be both upper-cased and snake-cased as you'd normally do.

# Run The Application From Command Line

You generally shouldn't need to do this since you can run the app from REPL. But just in case you'd like to do so:

```bash
$ SERVER_PORT=10000 lein run
```

# Create JAR File And Run The Application

Use this to package your app with all it's dependencies into a single JAR file and then to run it from the Docker container.

```bash
$ lein uberjar
$ SERVER_PORT=10000 java -jar target/uberjar/reaktor-codecamp-clj-0.1.0-SNAPSHOT-standalone.jar
```
