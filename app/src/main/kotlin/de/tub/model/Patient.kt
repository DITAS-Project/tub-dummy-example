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
