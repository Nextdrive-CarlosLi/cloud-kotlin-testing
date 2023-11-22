package nextdrive.cloud.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class TestingApplication

fun main(args: Array<String>) {
    runApplication<TestingApplication>(*args)

}
@RestController
@RequestMapping("/api")
class ResourceController constructor() {
    val limiter = RateLimiter(10, 10)

    @GetMapping("/resource")
    fun getResource(@RequestParam("name") name: String): ResponseEntity<String> =
        if (limiter.tryAcquire()) ResponseEntity.ok("Hello, $name!")
        else ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests!")
}
