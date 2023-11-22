package nextdrive.cloud.testing

import java.util.concurrent.atomic.AtomicLong
import java.util.function.LongUnaryOperator

class RateLimiter(private val capacity: Long, private val ratePerMinute: Long) {
    private val tokens: AtomicLong = AtomicLong(capacity)
    private val ratePerMillis: Long = ratePerMinute * 60 * 1000
    private var lastRefillTime: Long = System.currentTimeMillis()

    fun tryAcquire(): Boolean {
        refill()
        return tokens.getAndUpdate { tokens: Long -> if (tokens > 0) tokens - 1 else tokens } > 0
    }

    private fun refill() {
        val currentTime = System.currentTimeMillis()
        val elapsedMillis = currentTime - lastRefillTime
        val elapsedMinute = elapsedMillis / 60000
        val tokensToAdd = elapsedMinute * ratePerMinute
        println("$elapsedMillis")
        println("TokensToAdd: $tokensToAdd")
        if (tokensToAdd > 0) {
            lastRefillTime = currentTime
            tokens.updateAndGet { tokens: Long -> Math.min(capacity, tokens + tokensToAdd) }
        }
    }


}