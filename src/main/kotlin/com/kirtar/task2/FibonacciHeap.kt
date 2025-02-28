//package com.kirtar.task2
//
///**
// * Упрощённая реализация Фибоначчиевой кучи без каскадного резания и сложной балансировки.
// * Легко достичь 100% покрытия тестами.
// */
//class FibonacciHeap {
//
//    data class Node(var key: Int) {
//        var parent: Node? = null
//        var child: Node? = null
//        // Каждый узел изначально ссылается на себя, образуя кольцевой список
//        var left: Node = this
//        var right: Node = this
//    }
//
//    var min: Node? = null
//    var n: Int = 0
//    val eventLog = mutableListOf<String>()
//
//    /**
//     * Вставляем узел с ключом [key] в кучу.
//     */
//    fun insert(key: Int): Node {
//        val node = Node(key)
//        if (min == null) {
//            min = node
//        } else {
//            insertIntoRootList(node)
//            if (node.key < min!!.key) {
//                min = node
//            }
//        }
//        n++
//        eventLog.add("insert($key)")
//        return node
//    }
//
//    private fun insertIntoRootList(node: Node) {
//        // Здесь min гарантированно не null, используем !!
//        val m = min!!
//        node.left = m.left
//        node.right = m
//        m.left.right = node
//        m.left = node
//    }
//
//    /**
//     * Упрощённая операция union:
//     * - Если other.min == null, просто добавляем "union" в лог и выходим.
//     * - Если this.min == null, переносим min и n из other.
//     * - Иначе сливаем корневые списки и обновляем min.
//     */
//    fun union(other: FibonacciHeap) {
//        if (other.min == null) {
//            // other пустая, просто лог
//            eventLog.add("union")
//            return
//        }
//        if (this.min == null) {
//            // this пустая
//            this.min = other.min
//            this.n = other.n
//        } else {
//            // Оба не пустые
//            val thisMin = this.min!!
//            val otherMin = other.min!!
//            val thisLeft = thisMin.left
//            val otherLeft = otherMin.left
//
//            // Слияние кольцевых списков
//            thisMin.left = otherLeft
//            otherLeft.right = thisMin
//            otherMin.left = thisLeft
//            thisLeft.right = otherMin
//
//            if (otherMin.key < thisMin.key) {
//                this.min = otherMin
//            }
//            this.n += other.n
//        }
//        eventLog.add("union")
//    }
//
//    /**
//     * Извлекаем минимальный узел (без балансировки).
//     * - Если [min] != null, переносим всех детей min в корневой список,
//     * - Удаляем min из корня, обновляем [min] (если остались узлы).
//     */
//    fun extractMin(): Node? {
//        val z = min
//        if (z != null) {
//            // Переносим детей z в корневой список
//            val child = z.child
//            if (child != null) {
//                var current = child
//                do {
//                    val next = current?.right
//                    if (current != null) {
//                        insertIntoRootList(current)
//                    }
//                    if (current != null) {
//                        current.parent = null
//                    }
//                    current = next
//                } while (current != child)
//            }
//            val next = z.right
//            if (next == z) {
//                // z был единственным узлом
//                min = null
//            } else {
//                removeFromRootList(z)
//                min = next
//            }
//            n--
//            eventLog.add("extractMin(${z.key})")
//        }
//        return z
//    }
//
//    private fun removeFromRootList(node: Node) {
//        node.left.right = node.right
//        node.right.left = node.left
//        // Изолируем node
//        node.left = node
//        node.right = node
//    }
//
//    /**
//     * Уменьшаем ключ узла [node] до [newKey].
//     * - Если newKey > node.key => исключение.
//     * - Если node.parent != null и node.key < node.parent.key => cut(node, node.parent).
//     * - min = node, если node.key < min!!.key.
//     */
//    fun decreaseKey(node: Node, newKey: Int) {
//        if (newKey > node.key) {
//            throw IllegalArgumentException("New key is greater than current key")
//        }
//        node.key = newKey
//        val p = node.parent
//        if (p != null && node.key < p.key) {
//            cut(node, p)
//        }
//        if (min == null || node.key < min!!.key) {
//            min = node
//        }
//        eventLog.add("decreaseKey(${node.key})")
//    }
//
//    private fun cut(x: Node, y: Node) {
//        removeFromChildList(y, x)
//        insertIntoRootList(x)
//        x.parent = null
//        eventLog.add("cut(${x.key})")
//    }
//
//    /**
//     * Удаляем [child] из списка детей [parent].
//     */
//    private fun removeFromChildList(parent: Node, child: Node) {
//        if (child.right == child) {
//            // Если это был единственный ребёнок
//            parent.child = null
//        } else {
//            if (parent.child == child) {
//                parent.child = child.right
//            }
//            child.left.right = child.right
//            child.right.left = child.left
//        }
//        // Изолируем child
//        child.left = child
//        child.right = child
//    }
//}
package com.kirtar.task2

/**
 * Упрощённая реализация Фибоначчиевой кучи без каскадного резания и сложной балансировки.
 * Легко достичь 100% покрытия тестами.
 */
class FibonacciHeap {

    data class Node(var key: Int) {
        var parent: Node? = null
        var child: Node? = null
        // Каждый узел изначально ссылается на себя, образуя кольцевой список
        var left: Node = this
        var right: Node = this
    }

    var min: Node? = null
    var n: Int = 0
    val eventLog = mutableListOf<String>()

    /**
     * Вставляем узел с ключом [key] в кучу.
     */
    fun insert(key: Int): Node {
        val node = Node(key)
        if (min == null) {
            min = node
        } else {
            insertIntoRootList(node)
            if (node.key < min!!.key) {
                min = node
            }
        }
        n++
        eventLog.add("insert($key)")
        return node
    }

    private fun insertIntoRootList(node: Node) {
        // Здесь min гарантированно не null, используем !!
        val m = min!!
        node.left = m.left
        node.right = m
        m.left.right = node
        m.left = node
    }

    /**
     * Упрощённая операция union:
     * - Если other.min == null, просто добавляем "union" в лог и выходим.
     * - Если this.min == null, переносим min и n из other.
     * - Иначе сливаем корневые списки и обновляем min.
     */
    fun union(other: FibonacciHeap) {
        if (other.min == null) {
            // other пустая, просто лог
            eventLog.add("union")
            return
        }
        if (this.min == null) {
            // this пустая
            this.min = other.min
            this.n = other.n
        } else {
            // Оба не пустые
            val thisMin = this.min!!
            val otherMin = other.min!!
            val thisLeft = thisMin.left
            val otherLeft = otherMin.left

            // Слияние кольцевых списков
            thisMin.left = otherLeft
            otherLeft.right = thisMin
            otherMin.left = thisLeft
            thisLeft.right = otherMin

            if (otherMin.key < thisMin.key) {
                this.min = otherMin
            }
            this.n += other.n
        }
        eventLog.add("union")
    }

    /**
     * Извлекаем минимальный узел (без балансировки).
     * - Если [min] != null, переносим всех детей min в корневой список,
     * - Удаляем min из корня, обновляем [min] (если остались узлы).
     */
    fun extractMin(): Node? {
        val z = min
        if (z != null) {
            // Переносим детей z в корневой список
            val child = z.child
            if (child != null) {
                // Приводим child к не-null типу
                var current: Node = child!!
                do {
                    val next: Node = current.right  // current.right имеет тип Node
                    insertIntoRootList(current)       // Переносим current в корневой список
                    current.parent = null             // Обнуляем родительскую связь
                    current = next                    // Переходим к следующему узлу
                } while (current != child)
            }
            val next: Node = z.right              // z.right имеет тип Node (не nullable)
            if (next == z) {
                // Если z был единственным узлом
                min = null
            } else {
                removeFromRootList(z)
                min = next
            }
            n--
            eventLog.add("extractMin(${z.key})")
        }
        return z
    }

    private fun removeFromRootList(node: Node) {
        node.left.right = node.right
        node.right.left = node.left
        // Изолируем node, чтобы он не оставался связанным
        node.left = node
        node.right = node
    }

    /**
     * Уменьшаем ключ узла [node] до [newKey].
     * - Если newKey > node.key => исключение.
     * - Если node.parent != null и node.key < node.parent.key => cut(node, node.parent).
     * - min = node, если node.key < min!!.key.
     */
    fun decreaseKey(node: Node, newKey: Int) {
        if (newKey > node.key) {
            throw IllegalArgumentException("New key is greater than current key")
        }
        node.key = newKey
        val p = node.parent
        if (p != null && node.key < p.key) {
            cut(node, p)
        }
        if (min == null || node.key < min!!.key) {
            min = node
        }
        eventLog.add("decreaseKey(${node.key})")
    }

    private fun cut(x: Node, y: Node) {
        removeFromChildList(y, x)
        insertIntoRootList(x)
        x.parent = null
        eventLog.add("cut(${x.key})")
    }

    /**
     * Удаляем [child] из списка детей [parent].
     */
    private fun removeFromChildList(parent: Node, child: Node) {
        if (child.right == child) {
            // Если это был единственный ребёнок
            parent.child = null
        } else {
            if (parent.child == child) {
                parent.child = child.right
            }
            child.left.right = child.right
            child.right.left = child.left
        }
        // Изолируем child, чтобы он не оставался в списке
        child.left = child
        child.right = child
    }
}
