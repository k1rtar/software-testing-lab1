package com.kirtar.task3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun `test Person data class coverage`() {
        val person = Person(name = "John", traits = listOf("brave", "kind"))

        assertEquals("John", person.name)
        assertEquals(listOf("brave", "kind"), person.traits)


        person.state = State.Drumming
        assertEquals(State.Drumming, person.state)

        val person2 = person.copy(name = "Alice", traits = listOf("clever"), state = State.HandRemoved)
        // Проверяем, что скопированный объект действительно имеет новые значения
        assertEquals("Alice", person2.name)
        assertEquals(listOf("clever"), person2.traits)
        assertEquals(State.HandRemoved, person2.state)

        // 4) Вызываем equals(...) и hashCode()
        // Для equals: person != person2, так как у них разные name/traits/state
        assertNotEquals(person, person2)
        assertNotEquals(person.hashCode(), person2.hashCode())

        // 5) Вызываем toString()
        val str = person2.toString()
        // Проверяем, что toString() содержит имя "Alice"
        assertTrue(str.contains("Alice"))
    }
}
