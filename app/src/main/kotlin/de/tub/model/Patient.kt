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

package de.tub.model
/**
 * 
 * @param SSN 
 * @param lastName 
 * @param name 
 * @param gender 
 * @param mail 
 * @param birthday 
 */
data class Patient (
    val SSN: kotlin.Int? = null,
    val lastName: kotlin.String? = null,
    val name: kotlin.String? = null,
    val gender: Patient.Gender? = null,
    val mail: kotlin.String? = null,
    val birthday: kotlin.String? = null
) {

    enum class Gender(val value: kotlin.String) {
        m("m"),
        f("f"),
        undefined("undefined");
    }


}
