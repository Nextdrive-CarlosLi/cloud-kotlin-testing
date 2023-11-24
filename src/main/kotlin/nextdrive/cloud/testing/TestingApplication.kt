package nextdrive.cloud.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

@SpringBootApplication
class TestingApplication

fun main(args: Array<String>) {
    runApplication<TestingApplication>(*args)
}

@RestController
@RequestMapping("/api")
class ResourceController {
    private val IpRateLimiterMap = ConcurrentHashMap<String, RateLimiter>()
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        val SuccessMessage = "Congrats! Resource Acquired!"
    }

    @GetMapping("/resource")
    fun getResource(request: ServerHttpRequest): ResponseEntity<String> {
        // ip address as the key
        val ip = request.remoteAddress?.address.toString()

        val limiter: RateLimiter = IpRateLimiterMap.computeIfAbsent(ip) { _ -> RateLimiter(10, 10) }

        return if (limiter.tryAcquire()) {
            val remainingTokens = limiter.getRemainingTokens()
            ResponseEntity.ok().header("X-Rate-Limit-Remaining", remainingTokens.toString())
                .body(SuccessMessage)
        } else {
            logger.info("IP $ip is making too many requests!")
            val waitForRefill = limiter.getTimeToRefill()
            ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-Rate-Limit-Retry-After-Seconds", "$waitForRefill")
                .body("You(IP: $ip) are making too many requests! Please wait for $waitForRefill seconds to retry!")
        }
    }
}
