package com.kirtar.task2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FibonacciHeapTest {

    /**
     * Тестируем insert и extractMin для:
     * - пустой кучи,
     * - кучи с одним элементом,
     * - кучи с несколькими элементами.
     */
    @Test
    fun `test insert and extractMin`() {
        val heap = FibonacciHeap()
        // Проверяем, что extractMin из пустой кучи возвращает null
        assertNull(heap.extractMin(), "extractMin на пустой куче должен вернуть null")

        // Вставляем один элемент
        val single = heap.insert(100)
        assertEquals(100, heap.min?.key)
        assertEquals(1, heap.n)

        val extracted = heap.extractMin()
        assertEquals(100, extracted?.key)
        assertNull(heap.min, "После извлечения единственного элемента куча пуста")
        assertEquals(0, heap.n)

        // Вставляем несколько элементов
        heap.insert(10)
        heap.insert(5)
        heap.insert(20)
        assertEquals(3, heap.n)
        val minNode = heap.extractMin()
        assertEquals(5, minNode?.key, "Минимум должен быть равен 5")
        assertEquals(2, heap.n)
        assertNotNull(heap.min, "После извлечения узла 5, куча не пуста")
    }

    /**
     * Тест extractMin, когда у min ровно один ребёнок.
     * В этом случае цикл do-while выполняется один раз.
     */
    @Test
    fun `test extractMin single child`() {
        val heap = FibonacciHeap()
        val root = heap.insert(100)

        // Создаём единственного ребёнка
        val child = FibonacciHeap.Node(200)
        root.child = child
        child.parent = root

        // Устанавливаем, что в куче изначально 1 узел (root)
        heap.min = root
        heap.n = 1

        val extracted = heap.extractMin()
        assertEquals(100, extracted?.key, "Извлекаем root с ключом 100")
        // После удаления root n уменьшается на 1 => 0
        assertEquals(0, heap.n, "n стало 0")
        // Ребёнок перенесён в корневой список и становится новым min
        assertNotNull(heap.min, "Ребёнок становится min, хотя n=0")
        assertEquals(200, heap.min?.key, "min должен указывать на узел с ключом 200")
    }

    /**
     * Тест extractMin, когда у min есть несколько детей.
     */
    @Test
    fun `test extractMin with multiple children`() {
        val heap = FibonacciHeap()
        val root = heap.insert(50)
        val child1 = heap.insert(51)
        val child2 = heap.insert(52)

        // Связываем дочерние узлы с root
        root.child = child1
        child1.parent = root
        child2.parent = root

        // Формируем кольцевой список детей: child1 <-> child2
        child1.right = child2
        child2.left = child1
        child2.right = child1
        child1.left = child2

        val extracted = heap.extractMin()
        assertEquals(50, extracted?.key, "Извлекаем root с ключом 50")
        // После извлечения root остаются 2 узла
        assertEquals(2, heap.n, "В куче должны остаться child1 и child2")
        assertNotNull(heap.min)
        val mk = heap.min?.key
        assertTrue(mk == 51 || mk == 52, "Минимальный ключ должен быть 51 или 52")

        val log = heap.eventLog.joinToString(",")
        assertTrue(log.contains("extractMin(50)"), "Лог должен содержать extractMin(50)")
    }

    /**
     * Тест для union:
     * 1) union с пустой кучей.
     * 2) Если this.min == null, а other.min != null.
     * 3) Оба не null, и другой min меньше текущего.
     * 4) Оба не null, и другой min больше или равен.
     */
    @Test
    fun `test union`() {
        // 1) union с пустой кучей
        val h1 = FibonacciHeap()
        val h2 = FibonacciHeap()
        h1.union(h2)
        assertTrue(h1.eventLog.contains("union"), "union должен быть в логе")

        // 2) Если текущая куча пуста, а другая не пуста
        val h3 = FibonacciHeap()
        val h4 = FibonacciHeap()
        h4.insert(10)
        h3.union(h4)
        assertEquals(10, h3.min?.key)
        assertEquals(1, h3.n)
        assertTrue(h3.eventLog.contains("union"))

        // 3) Оба не пустые, другой min меньше
        val h5 = FibonacciHeap()
        val h6 = FibonacciHeap()
        h5.insert(50)
        h6.insert(5)
        h5.union(h6)
        assertEquals(5, h5.min?.key, "min должен стать 5")
        assertEquals(2, h5.n)
        assertTrue(h5.eventLog.contains("union"))

        // 4) Оба не пустые, другой min больше или равен
        val h7 = FibonacciHeap()
        val h8 = FibonacciHeap()
        h7.insert(3)
        h8.insert(10)
        h7.union(h8)
        assertEquals(3, h7.min?.key, "min остаётся 3")
        assertEquals(2, h7.n)
        assertTrue(h7.eventLog.contains("union"))
    }

    /**
     * Тест decreaseKey:
     * - Проверка исключения при newKey > node.key.
     * - Проверка cut, когда новый ключ меньше родительского.
     */
    @Test
    fun `test decreaseKey`() {
        val heap = FibonacciHeap()
        val n20 = heap.insert(20)
        val n10 = heap.insert(10)

        assertThrows(IllegalArgumentException::class.java) {
            heap.decreaseKey(n10, 50)
        }

        // Создаем узел n30 и назначаем ему фиктивного родителя для вызова cut
        val n30 = heap.insert(30)
        val fakeParent = FibonacciHeap.Node(999)
        n30.parent = fakeParent
        fakeParent.child = n30

        heap.decreaseKey(n30, 10)
        val log = heap.eventLog.joinToString(",")
        assertTrue(log.contains("cut(10)"), "Ожидаем cut(10) после decreaseKey")

        heap.decreaseKey(n30, 1)
        assertEquals(1, n30.key)
        assertEquals(n30, heap.min)
    }

    @Test
    fun `test decreaseKey when min is null`() {
        val heap = FibonacciHeap()

        // Создаём узел вручную
        val node = FibonacciHeap.Node(100)

        // Принудительно говорим, что в куче есть 1 узел, но heap.min = null
        // (искусственный сценарий, нужный только для покрытия)
        heap.n = 1
        heap.min = null

        // У node нет parent, child, но для decreaseKey это не критично.
        // Вызываем decreaseKey: сработает ветвь if (min == null) { min = node }
        heap.decreaseKey(node, 50)

        // Проверяем, что min теперь указывает на node
        assertEquals(node, heap.min, "После decreaseKey, min должен стать node")
        assertEquals(50, node.key, "Ключ должен уменьшиться до 50")
    }


    /**
     * Тест decreaseKey, когда cut не происходит (node.key >= p.key)
     * и случай, когда newKey == node.key.
     */
    @Test
    fun `test decreaseKey no cut scenario`() {
        val heap = FibonacciHeap()
        val p = FibonacciHeap.Node(10)
        val c = FibonacciHeap.Node(20)
        p.child = c
        c.parent = p
        heap.min = p
        heap.n = 1

        // newKey == node.key
        heap.decreaseKey(c, 20)
        assertEquals(p, c.parent)
        assertEquals(p, heap.min)

        // newKey (15) всё ещё >= p.key (10)
        heap.decreaseKey(c, 15)
        assertEquals(p, c.parent)
        assertEquals(p, heap.min)
    }

    /**
     * Тест removeFromChildList для случая с 3 детьми (удаляем узел из середины, затем голову, затем единственного ребенка).
     */
    @Test
    fun `test removeFromChildList multiple children removing middle`() {
        val heap = FibonacciHeap()
        val p = FibonacciHeap.Node(999)

        val c1 = FibonacciHeap.Node(1)
        val c2 = FibonacciHeap.Node(2)
        val c3 = FibonacciHeap.Node(3)

        // Связываем в кольцевой список: c1 -> c2 -> c3 -> c1
        c1.right = c2
        c2.left = c1
        c2.right = c3
        c3.left = c2
        c3.right = c1
        c1.left = c3

        p.child = c1
        c1.parent = p
        c2.parent = p
        c3.parent = p

        val method = heap.javaClass.getDeclaredMethod(
            "removeFromChildList",
            FibonacciHeap.Node::class.java,
            FibonacciHeap.Node::class.java
        )
        method.isAccessible = true

        // Удаляем c2 (середина)
        method.invoke(heap, p, c2)
        assertEquals(c1, p.child, "p.child остаётся c1")
        assertEquals(c1, c3.right)
        assertEquals(c3, c1.left)

        // Удаляем c1 (голова), теперь p.child должно стать c3
        method.invoke(heap, p, c1)
        assertEquals(c3, p.child, "После удаления c1, p.child = c3")
        assertEquals(c3, c3.right)
        assertEquals(c3, c3.left)

        // Удаляем c3 (единственный)
        method.invoke(heap, p, c3)
        assertNull(p.child, "После удаления c3, p.child = null")
    }

    /**
     * Тест removeFromChildList для случая с 2 детьми.
     */
    @Test
    fun `test removeFromChildList`() {
        val heap = FibonacciHeap()
        val p = FibonacciHeap.Node(100)
        val c1 = FibonacciHeap.Node(101)
        val c2 = FibonacciHeap.Node(102)
        p.child = c1
        c1.parent = p
        c2.parent = p
        c1.right = c2
        c2.left = c1
        c2.right = c1
        c1.left = c2

        val method = heap.javaClass.getDeclaredMethod(
            "removeFromChildList",
            FibonacciHeap.Node::class.java,
            FibonacciHeap.Node::class.java
        )
        method.isAccessible = true

        method.invoke(heap, p, c1)
        assertEquals(c2, p.child, "После удаления c1, p.child должен быть c2")

        method.invoke(heap, p, c2)
        assertNull(p.child, "После удаления c2, p.child должен быть null")
    }

    /**
     * Проверяем вызовы геттеров (min, n, eventLog).
     */
    @Test
    fun `test getters coverage`() {
        val heap = FibonacciHeap()
        heap.insert(10)
        heap.insert(20)

        val currentMin = heap.min
        val count = heap.n
        val log = heap.eventLog

        assertNotNull(currentMin, "min не должен быть null")
        assertTrue(count > 0, "n должно быть больше 0")
        assertTrue(log.isNotEmpty(), "eventLog не должен быть пустым")
    }

    /**
     * Проверяем сеттеры для свойств n и min.
     */
    @Test
    fun `test set properties for coverage`() {
        val heap = FibonacciHeap()
        heap.n = 5
        assertEquals(5, heap.n, "Сеттер n должен установить значение 5")

        val newNode = FibonacciHeap.Node(999)
        heap.min = newNode
        assertEquals(999, heap.min?.key, "Сеттер min должен установить узел с ключом 999")

        heap.n = 10
        heap.min = null
        assertEquals(10, heap.n, "n должно быть равно 10")
        assertNull(heap.min, "min должно быть null")
    }
}
