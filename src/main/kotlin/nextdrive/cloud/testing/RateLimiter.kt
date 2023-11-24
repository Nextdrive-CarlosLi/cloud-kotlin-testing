package nextdrive.cloud.testing

import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.roundToInt

// Simple Token bucket algorithm
class RateLimiter(private val capacity: Long, private val ratePerMinute: Long) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val tokens: AtomicLong = AtomicLong(capacity)
    private var lastRefillTime: Long = System.currentTimeMillis()

    fun tryAcquire(): Boolean {
        refill()
        return tokens.getAndUpdate { tokens: Long -> if (tokens > 0) tokens - 1 else tokens } > 0
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    private fun refill() {
        val currentTime = System.currentTimeMillis()
        val elapsedMillis = currentTime - lastRefillTime
        val elapsedMinute = elapsedMillis / 60000.0
        val tokensToAdd = elapsedMinute.roundToInt() * ratePerMinute
        logger.info("Elapsed minute: ${elapsedMinute.format(2)}")
        logger.info("Tokens to add: $tokensToAdd")

        // Refill
        if (tokensToAdd > 0) {
            lastRefillTime = currentTime
            tokens.updateAndGet { tokens: Long -> capacity.coerceAtMost(tokens + tokensToAdd) }
        }
    }

    fun getRemainingTokens(): AtomicLong {
        return tokens
    }

    fun getTimeToRefill(): Long {
        val elapsedMillis = System.currentTimeMillis() - lastRefillTime
        logger.info("Milliseconds since last refill: $elapsedMillis")
        return Duration.ofMillis(60000).minusMillis(elapsedMillis).toSeconds()
    }
}