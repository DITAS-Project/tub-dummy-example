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

class VDCClientIntegrationTest {
    
    companion object {
        var sev: NettyApplicationEngine? = null;

        val patient = """
            {
                "SSN": 12,
                "lastName": "Werlinger",
                "name": "Bibi",
                "gender": "m",
                "mail": "Bibi.Werlinger@web.de",
                "birthday": "1995-07-25"
            }
        """.trimIndent()
        val exam = """
            {
                "SSN": 2,
                "lastName": "Krush",
                "name": "Terry",
                "date": 835810597000,
                "cholesterol": 0.94,
                "triglyceride": 2.82,
                "hepatitis": true
            }
        """.trimIndent()

        val examobj = Exam(2 ,"Krush", "Terry", "835810597000", 0.94,2.82, true)

        @BeforeClass
        @JvmStatic
        fun server() {
            sev = embeddedServer(Netty, 9999) {
                routing {
                    get("/exam/{}") {
                        call.respondText("[$exam]", ContentType.Application.Json)
                    }
                    get("/patient/{}") {
                        call.respondText(patient, ContentType.Application.Json)
                    }
                    get("/find") {
                        call.respondText("[$exam]", ContentType.Application.Json)
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
        val rpatient = Patient(12, "Werlinger", "Bibi", Patient.Gender.m, "Bibi.Werlinger@web.de", "1995-07-25")
        val cpatient = client.getPatient(12)
        assertNotNull(cpatient)
        assertEquals(cpatient,rpatient)
    }

    @Test
    fun getExams() {
        assertTrue(client.getExams().contains(examobj))
    }

    @Test
    fun getExam() {
        assertTrue(client.getExams().contains(examobj))
    }
}