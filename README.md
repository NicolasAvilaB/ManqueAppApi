# ManqueAppApi

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                                                     | Description                                                                        |
|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Routing](https://start.ktor.io/p/routing)                                                               | Provides a structured routing DSL                                                  |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization)                                   | Handles JSON serialization using kotlinx.serialization library                     |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)                                       | Provides automatic content conversion according to Content-Type and Accept headers |
| [Ktor Server Extensions](https://github.com/reactor/reactor-kotlin-extensions)                           | Provides Kotlin Coroutine extensions for better interop with Project Reactor types                                                                                |
| [Mysql R2dbc](https://github.com/asyncer-io/r2dbc-mysql)                                                 | A non-blocking, reactive MySQL driver based on the R2DBC specification                                                                                  |
| [Reactor Core](https://github.com/reactor/reactor-core)                                                  | Core library for building reactive applications on the JVM using the Reactive Streams API                                                                                |
| [Kotlinx Reactor](https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-reactor)   | Bridges Kotlin Coroutines with Reactor types like Mono and Flux                                                                                   |
| [Kotlinx Reactive](https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-reactive) | Adapters for integrating Kotlin Coroutines with Reactive Streams (Publisher, Subscriber, etc.)                                                                           |                                                                                    |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|----------------------------------------------------------------------|
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything and generate Jar on build/libs folder               |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output example:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

