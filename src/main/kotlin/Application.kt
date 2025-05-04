package com.manque.app

import data.model.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import plugins.configureRouting
import plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    embeddedServer(Netty) {
        configureSerialization()
        configureRouting()
        DatabaseFactory.init()
    }.start(wait = true)
}
