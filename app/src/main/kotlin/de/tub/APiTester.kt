package de.tub

import de.tub.api.APIException
import de.tub.api.VDCClient
import mu.KotlinLogging
import java.util.*
import kotlin.system.measureTimeMillis

val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val client = VDCClient("localhost", 8080)

    logger.info("starting test run on http://{}:{}",client.host,client.port)

    val rand:Random = Random()

    var waitTime:Long = 0
    while (true){
        performAPITest(rand, client)
        waitTime = rand.nextInt(20)*1000L
        logger.info ("perfroming next round in {}s",waitTime/1000)
        Thread.sleep(waitTime)
    }

}

private fun performAPITest(rand: Random, client: VDCClient) {
    val requests = 1000

    var fail = 0
    var success = 0

    val getPatient = measureTimeMillis({
        for (i in 1..requests) {
            val ssn = rand.nextInt(50)
            try {
                client.getPatient(ssn)
                success++
            } catch (e: APIException) {
                fail++
            }
        }
    })
    logger.info("getPatient for {} runs took {}ms with {} success and {} failed. {}ms per request", requests, getPatient, success, fail, (getPatient/requests))

    fail = 0;
    success = 0;

    val getExam = measureTimeMillis({
        for (i in 1..requests) {
            val ssn = rand.nextInt(50)
            try {
                client.getExams(ssn)
                success++
            } catch (e: APIException) {
                fail++
            }
        }
    })
    logger.info("getExam for {} runs took {}ms with {} success and {} failed {}ms per request", requests, getExam, success, fail, (getExam/requests))

    fail = 0;
    success = 0;

    val find = measureTimeMillis({
        for (i in 1..requests) {

            try {
                var maxAge: Int? = null
                var minAge: Int? = null
                if (rand.nextBoolean()) {
                    minAge = rand.nextInt(100)
                    maxAge = minAge + rand.nextInt(100)
                }
                var gender: Char = 'f'
                if (rand.nextBoolean()) {
                    gender = 'm'
                }



                client.getExams(minAge, maxAge, gender)
                success++
            } catch (e: APIException) {
                fail++
            }
        }
    })
    logger.info("find for {} runs took {}ms with {} success and {} failed {}ms per request", requests, find, success, fail, (find/requests))
}