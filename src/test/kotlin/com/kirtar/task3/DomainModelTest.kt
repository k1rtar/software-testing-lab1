package com.kirtar.task3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DomainModelTest {

    @Test
    fun `test state transitions for person`() {
        val person = Person(name = "Operator", traits = listOf("impulsive", "arrogant"))
        val stateMachine = StateMachine(person)

        assertEquals(State.Idle, person.state)


        stateMachine.performAction(Action.StartDrumming)
        assertEquals(State.Drumming, person.state)


        stateMachine.performAction(Action.RemoveHand)
        assertEquals(State.HandRemoved, person.state)


        stateMachine.performAction(Action.Reset)
        assertEquals(State.Idle, person.state)


        stateMachine.performAction(Action.StartDrumming)
        stateMachine.performAction(Action.TriggerExplosion)
        assertEquals(State.Explosive, person.state)
    }

    @Test
    fun `test actions do nothing in invalid states`() {
        val person = Person(name = "Test", traits = listOf("random"))
        val fsm = StateMachine(person)


        fsm.performAction(Action.RemoveHand)
        assertEquals(State.Idle, person.state, "RemoveHand не должно менять состояние из Idle")


        fsm.performAction(Action.TriggerExplosion)
        assertEquals(State.Idle, person.state, "TriggerExplosion не должно менять состояние из Idle")


        fsm.performAction(Action.StartDrumming)
        assertEquals(State.Drumming, person.state)


        fsm.performAction(Action.StartDrumming)
        assertEquals(State.Drumming, person.state, "StartDrumming не должно сработать из Drumming")
    }

    @Test
    fun `test person data class coverage`() {
        val person1 = Person(name = "Bob", traits = listOf("brave"))
        val person2 = person1.copy(name = "Alice")
        assertNotEquals(person1, person2)
        assertNotEquals(person1.hashCode(), person2.hashCode())


        val str = person1.toString()
        assertTrue(str.contains("Bob"))
    }

    @Test
    fun `test Action enum coverage`() {
        val actions = Action.values()
        assertTrue(actions.contains(Action.StartDrumming))
        assertTrue(actions.contains(Action.RemoveHand))
        assertTrue(actions.contains(Action.TriggerExplosion))
        assertTrue(actions.contains(Action.Reset))

        assertEquals(4, actions.size)


        val start = Action.valueOf("StartDrumming")
        assertEquals(Action.StartDrumming, start)


        assertThrows(IllegalArgumentException::class.java) {
            Action.valueOf("NonExistingAction")
        }
    }
    @Test
    fun `test Action enum coverage for getEntries`() {

        val allActions = Action.entries

        assertEquals(4, allActions.size)

        assertTrue(allActions.contains(Action.StartDrumming))
        assertTrue(allActions.contains(Action.RemoveHand))
        assertTrue(allActions.contains(Action.TriggerExplosion))
        assertTrue(allActions.contains(Action.Reset))


        val start = Action.valueOf("StartDrumming")
        assertEquals(Action.StartDrumming, start)


        assertThrows(IllegalArgumentException::class.java) {
            Action.valueOf("UnknownAction")
        }
    }




}
