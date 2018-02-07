package de.tub.api

import com.fasterxml.jackson.databind.ObjectMapper
import de.tub.model.Exam
import de.tub.model.Patient

import org.junit.Assert.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.respondText
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import org.junit.*
import java.util.*
import java.util.concurrent.TimeUnit

class VDCClientTest {

    companion object {
        var sev: NettyApplicationEngine? = null;

        val patient = Patient(12, "Werlinger", "Bibi", Patient.Gender.m, "Bibi.Werlinger@web.de", "1995-07-25")
        val exam = Exam(12, "Werlinger", "Bibi", "1460735006000", 3.44, 1.86, true)

        @BeforeClass
        @JvmStatic
        fun server() {
            val m = ObjectMapper()

            sev = embeddedServer(Netty, 9999) {
                routing {
                    get("/exam/{}") {
                        call.respondText(m.writeValueAsString(Collections.singleton(exam)), ContentType.Application.Json)
                    }
                    get("/patient/{}") {
                        call.respondText(m.writeValueAsString(patient), ContentType.Application.Json)
                    }
                    get("/find") {
                        call.respondText(m.writeValueAsString(Collections.singleton(exam)), ContentType.Application.Json)
                    }
                }
            }
            sev?.start(false)
        }

        @AfterClass
        @JvmStatic
        fun clean() {
            sev?.stop(0, 30, TimeUnit.SECONDS)
        }
    }


    lateinit var client:VDCClient;
    @Before
    fun setup() {
        client = VDCClient("localhost", 9999)
    }

    @Test
    fun getPatient() {
        assertEquals(client.getPatient(12),patient)
    }

    @Test
    fun getExams() {
        assertTrue(client.getExams().contains(exam))
    }

    @Test
    fun getExam() {
        assertTrue(client.getExams().contains(exam))
    }
}