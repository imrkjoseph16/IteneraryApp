package com.example.iteneraryapplication.app.util

import java.util.concurrent.CancellationException

@Suppress("RedundantSuspendModifier")
suspend inline fun <T, R> T.coRunCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}