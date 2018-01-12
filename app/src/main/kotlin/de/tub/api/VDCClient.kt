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

package de.tub.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.tub.model.Exam
import de.tub.model.Patient
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.time.LocalDate

class VDCClient(val host:String = "localhost",val port:Int) {
    private val client: OkHttpClient = OkHttpClient()

    private val mapper = jacksonObjectMapper()

    public fun getPatient(ssn:Int): Patient? {
        val url = HttpUrl.Builder().scheme("http").host(host).port(port)
                .addPathSegment("patient")
                .addPathSegment(ssn.toString()).build()
        val request = Request.Builder().get().url(url).build()

        val response = client.newCall(request).execute()

        if(response.code() == 200){
            return mapper.readValue<Patient>(response.body()!!.bytes())
        } else {
            throw APIException(response.body()?.string(),response.code())
        }
    }

    public fun getExams(ssn:Int): List<Exam> {
        val url = HttpUrl.Builder().scheme("http").host(host).port(port)
                .addPathSegment("exam")
                .addPathSegment(ssn.toString()).build()
        val request = Request.Builder().get().url(url).build()

        val response = client.newCall(request).execute()

        if(response.code() == 200){
            return mapper.readValue<List<Exam>>(response.body()!!.bytes())
        } else {
            throw APIException(response.body()?.string(),response.code())
        }
    }

    val df:SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

    public fun getExams(minAge:Int? = null,maxAge:Int?= null,gender:Char?= null,startDate:LocalDate?= null,endDate:LocalDate?= null): List<Exam> {
        val url = HttpUrl.Builder().scheme("http").host(host).port(port)
                .addPathSegment("find");
        if(minAge != null){
            url.addQueryParameter("minAge",minAge.toString());
        }
        if(maxAge != null){
            url.addQueryParameter("maxAge",maxAge.toString());
        }
        if(gender != null){
            url.addQueryParameter("gender",gender.toString());
        }
        if(startDate != null){
            url.addQueryParameter("startDate",df.format(startDate))
        }
        if(startDate != null){
            url.addQueryParameter("endDate",df.format(endDate))
        }

        val request = Request.Builder().get().url(url.build()).build()

        val response = client.newCall(request).execute()

        if(response.code() == 200){
            return mapper.readValue<List<Exam>>(response.body()!!.bytes())
        } else {
            throw APIException(response.body()?.string(),response.code())
        }
    }


}