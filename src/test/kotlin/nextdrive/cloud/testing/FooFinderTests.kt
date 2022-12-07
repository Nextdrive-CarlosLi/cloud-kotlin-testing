package nextdrive.cloud.testing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import nextdrive.cloud.testing.NextDrive
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

@SpringBootTest
class FooFinderTests {
    
    @Test
    fun `should find proper`() {
        val foojson = jacksonObjectMapper()
        foojson.registerKotlinModule()
        foojson.registerModule(JavaTimeModule())

        val fooString: String = (".\src\main\resources\foo.json").readText(Charsets.UTF_8)
        val fooTextList: List<Film> = foojson.readValue<List<Film>>(jsonString)
        for (film in fooTextList){
            val result = NextDrive.find(film)
            if(film=="eb947b41-1fc8-420a-95bf-404d2fb01a1b"){
                assertThat(result).allMatch { "0e0dfed3-79d4-46e5-a0b1-4645b42f4aee" }
            }else if(film=="423e5d8e-7d02-4b0c-b229-79683559d4fb"){
                assertThat(result).allMatch { "6fe58c5b-1f68-4b14-aa0e-9270b5f48153" }
            }else if(film=="8fc5d53a-df9e-4f73-8a36-5901e8ae0de3"){
                assertThat(result).allMatch { "09a6c4a4-eecd-4445-8b26-301ab2f8f391" }
            }else if(film=="18aef181-5e7e-4c2c-b5e6-8ca57e0e4a14"){
                assertThat(result).allMatch { "d1c647f9-afd4-416b-bb45-bd27693b5f9c" }
            }else if(film=="06e189fa-e93f-4139-93cd-d9dbdb2dbe0c"){
                assertThat(result).allMatch { "f66385e0-aa46-4d03-840c-0bc41135f7a8" }
            }else{
                return null
            }
        }
    }
}
