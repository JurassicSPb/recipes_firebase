package tools

abstract class Mapper<in From, out To> {

    abstract fun map(from: From): To

    fun map(from: List<From>) = from.map { map(it) }
}