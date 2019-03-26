package com.fish.readymanager

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashMap

class Observable<T>(initValue: T) {
    private val observers = Collections.synchronizedMap<String, Observer<T>>(HashMap<String, Observer<T>>())
    private val needWait = AtomicBoolean(false)
    var value = initValue
        set(value) {
            if (value != field) {

                field = value
                val listForRemove = mutableListOf<Observer<T>>()
                observers.values.forEach {
                    if (it !is OnceObserver) {
                        it.func.invoke(value)
                    } else {
                        if (it.waitingValue == value) {
                            listForRemove.add(it)
                            it.func.invoke(value)
                        }
                    }
                }
                listForRemove.forEach {
                    observers.remove(it.name)
                }
            }
        }

    fun addObserver(observer: Observer<T>) {
        if (observer !is OnceObserver) {
            observers[observer.name] = observer
            observer.func(value)
        } else {
            if (observer.waitingValue != value) {

                observers[observer.name] = observer
            } else {
                observer.func(value)
            }

        }

    }

    fun removeObserver(observer: Observer<T>) {
        observers.remove(observer.name)
    }

    fun removeObserver(name: String) {
        observers.remove(name)
    }

}

open class Observer<T>(val name: String, val func: (T) -> Unit) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Observer<*>

        return this.name == other.name
    }

    override fun hashCode() = name.hashCode()

}

class OnceObserver<T>(name: String, val waitingValue: T, func: (T) -> Unit) : Observer<T>(name, func)

fun <E> MutableCollection<E>.replace(element: E) {
    this.remove(element)
    this.add(element)
}