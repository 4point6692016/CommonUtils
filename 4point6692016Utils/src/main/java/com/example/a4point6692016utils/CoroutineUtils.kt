package com.example.a4point6692016utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> Lifecycle.async(
    callback: ((T?) -> Unit)?,
    function: suspend () -> T?,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    coroutineScope.launchWhenCreated {
        withContext(dispatcher) {
            try {
                function()
            } catch (exception: Exception) {
                null
            }
        }.also {
            callback?.invoke(it)
        }
    }
}

fun <T> asyncProcess(
    lifecycle: Lifecycle,
    asyncExecution: suspend () -> T,
    callback: (T?) -> Unit
) = lifecycle.async(callback, asyncExecution)

fun <T> asyncIOProcess(
    lifecycle: Lifecycle,
    asyncExecution: suspend () -> T,
    callback: (T?) -> Unit
) = lifecycle.async(callback, asyncExecution, Dispatchers.IO)

fun <T> asyncUnbounded(
    lifecycle: Lifecycle,
    asyncExecution: suspend () -> Unit
) = lifecycle.async(null, asyncExecution, Dispatchers.IO)

/**
 * This should fire and forget and shouldn't contain any UI access
 * Ex: Updating DB about a value. It runs in Application's LifeCycle
 */
fun runDBOperations(
    asyncExecution: suspend () -> Unit
) {
    GlobalScope.launch(Dispatchers.Default) {
        asyncExecution()
    }
}

fun runBackgroundTasks(
    asyncExecution: suspend () -> Unit
) {
    GlobalScope.launch(Dispatchers.Default) {
        asyncExecution()
    }
}

fun runIOOperations(
    asyncExecution: suspend () -> Unit
) {
    GlobalScope.launch(Dispatchers.IO) {
        asyncExecution()
    }
}

interface CompletedListener<T> {
    fun onComplete(value: T?)
}

/**
 * This is Launched in the Global Scope , make sure UseCases Suits This.
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T> runIOTaskInBackGround(
    function: suspend () -> T?,
    callback: ((T?) -> Unit)?
) {
    GlobalScope.launch {
        val result = runCatching {
            withContext(Dispatchers.IO) {
                function()
            }
        }
        result.onSuccess { value ->
            callback?.invoke(value)
        }
    }
}