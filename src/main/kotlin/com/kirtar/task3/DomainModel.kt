package com.kirtar.task3

/**
 * Определяем все возможные состояния персонажа.
 * Sealed-классы позволяют компилятору проверить исчерпывающий when.
 */
sealed class State {
    object Idle : State()           // исходное состояние
    object Drumming : State()       // барабанит пальцами по панели
    object HandRemoved : State()    // рука убрана, предотвращена опасность
    object Explosive : State()      // потенциально взрывоопасное состояние
}

/**
 * Доменная сущность, представляющая персонажа.
 */
data class Person(val name: String, val traits: List<String>, var state: State = State.Idle)

/**
 * Перечисление действий, вызывающих переходы состояний.
 */
enum class Action {
    StartDrumming,
    RemoveHand,
    TriggerExplosion,
    Reset
}

/**
 * Машина состояний (FSM) для управления переходами персонажа.
 */
class StateMachine(private val person: Person) {
    fun performAction(action: Action) {
        when (action) {
            Action.StartDrumming -> {
                if (person.state == State.Idle) {
                    person.state = State.Drumming
                }
            }
            Action.RemoveHand -> {
                if (person.state == State.Drumming) {
                    person.state = State.HandRemoved
                }
            }
            Action.TriggerExplosion -> {
                if (person.state == State.Drumming) {
                    person.state = State.Explosive
                }
            }
            Action.Reset -> {
                person.state = State.Idle
            }
        }
    }
}
