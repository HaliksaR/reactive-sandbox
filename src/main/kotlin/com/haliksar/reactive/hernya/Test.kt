package com.haliksar.reactive.hernya

interface Test {
    val test: String
    fun print() = println(test)
}

interface Test2 {
    val test2: String
    fun print2() = println(test2)
}

class TestImpl(override val test: String) : Test
class Test2Impl(override val test2: String) : Test2
class TestClass(builder: Test2.()-> Unit) : Test by TestImpl("1"), Test2 by Test2Impl("2").apply(builder) {

    override fun print() {
        super.print()
        println("TestClass print")
    }

    override fun print2() {
        super.print2()
        println("TestClass print2")
    }
}

class TestClassBezViebonov : Test, Test2  {
    override val test: String = "1"
    override val test2: String = "2"

    override fun print() {
        super.print()
        println("TestClass print")
    }

    override fun print2() {
        super.print2()
        println("TestClass print2")
    }
}

fun main() {
    val test: TestClass = TestClass {
        print2()
    }
    (test as Test).print()
}