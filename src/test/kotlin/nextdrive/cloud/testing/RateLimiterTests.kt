package nextdrive.cloud.testing

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
class RateLimiterTests {


    // test2: 00:00 ~ 00:59 token = 0(token remain = 0)

    // test3: bucket of two ips are independent/different

    lateinit var client: WebTestClient

    @BeforeEach
    fun initClient() {
        client = WebTestClient.bindToController(ResourceController()).build()
    }

    @Test
    fun `should return success status and message in response to a single call to the endpoint`() {
        client.get().uri("/api/resource")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo(ResourceController.SuccessMessage)
    }

    // test1: block after 10 req/minute are made
    @Test
    fun `should be rateLimited after ten consecutive calls to the endpoint`() {
        for (i in 1..10) {
            client.get().uri("/api/resource")
                .exchange()
                .expectStatus().isOk
                .expectBody<String>()
                .isEqualTo(ResourceController.SuccessMessage)
        }
        client.get().uri("/api/resource")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS)
    }


//    @AfterAll
//    fun tearDown() {
//    }
}
