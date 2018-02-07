/*
 * Copyright 2017 ISE TU Berlin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tub

import de.tub.api.APIException
import de.tub.api.VDCClient
import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarStyle
import mu.KotlinLogging
import java.util.*
import kotlin.system.measureTimeMillis

val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val host:String = if(args.isNotEmpty() && !args[0].isEmpty()){
        args[0]
    } else {
        "localhost"
    }

    val client = VDCClient(host, 8000)

    logger.info("starting test run on http://${client.host}:${client.port}")

    val rand:Random = Random()


    while (true){
        performAPITest(rand, client)
        val waitTime = rand.nextInt(20)*1000L
        logger.info ("performing next round in ${waitTime/1000}s")
        Thread.sleep(waitTime)
    }

}

private fun performAPITest(rand: Random, client: VDCClient) {
    val requests = 1000

    var fail = 0
    var success = 0
    var pb = ProgressBar("", requests.toLong(), ProgressBarStyle.ASCII)
    pb.start()
    pb.setExtraMessage("getPatient")
    val getPatient = measureTimeMillis({

        for (i in 1..requests) {
            val ssn = rand.nextInt(50)
            try {
                client.getPatient(ssn)
                success++
            } catch (e: APIException) {
                fail++
            }
            pb.step()
        }

    })

    logger.info("getPatient for {} runs took {}ms with {} success and {} failed. {}ms per request", requests, getPatient, success, fail, (getPatient/requests))

    fail = 0;
    success = 0;
    pb.stepTo(0)
    pb.setExtraMessage("getExam")
    val getExam = measureTimeMillis({

        for (i in 1..requests) {
            val ssn = rand.nextInt(50)
            try {
                client.getExams(ssn)
                success++
            } catch (e: APIException) {
                fail++
            }
            pb.step()
        }
    })

    logger.info("getExam for {} runs took {}ms with {} success and {} failed {}ms per request", requests, getExam, success, fail, (getExam/requests))

    fail = 0
    success = 0
    pb.stepTo(0)
    pb.setExtraMessage("find")
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
            pb.step()
        }

    })
    pb.stop()
    logger.info("find for {} runs took {}ms with {} success and {} failed {}ms per request", requests, find, success, fail, (find/requests))
}