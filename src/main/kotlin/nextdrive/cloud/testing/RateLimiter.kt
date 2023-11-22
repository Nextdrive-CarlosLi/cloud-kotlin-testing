package nextdrive.cloud.testing

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicLong

class RateLimiter(private val capacity: Long, private val ratePerMinute: Long) {
    private val logger = LoggerFactory.getLogger(javaClass)
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
        logger.info("Elapsed millisecond: $elapsedMillis")
        logger.info("Tokens to add: $tokensToAdd")
        if (tokensToAdd > 0) {
            lastRefillTime = currentTime
            tokens.updateAndGet { tokens: Long -> Math.min(capacity, tokens + tokensToAdd) }
        }
    }

    fun getRemainingTokens(): AtomicLong {
        return tokens
    }
}