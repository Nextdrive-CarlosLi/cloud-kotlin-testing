package nextdrive.cloud.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/resource")
    fun getResource(request: ServerHttpRequest): ResponseEntity<String> {
        val ip = request.remoteAddress?.address.toString()
        val limiter: RateLimiter = IpRateLimiterMap.computeIfAbsent(ip) { _ -> RateLimiter(10, 10) }

        return if (limiter.tryAcquire()) ResponseEntity.ok("Congrats! Resource Acquired!")
        else ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests!")
    }
}
