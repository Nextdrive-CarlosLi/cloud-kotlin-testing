package nextdrive.cloud.testing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
class RateLimiterTests {

    @Test
    fun `should find proper`() {
        // TODO
    }

    val client = WebTestClient.bindToController(ResourceController())
        .configureClient()
        .baseUrl("/api/resource")
        .build()

}
