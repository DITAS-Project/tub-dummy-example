package de.tub.model
/**
 * 
 * @param SSN 
 * @param lastName 
 * @param name 
 * @param date 
 * @param cholesterol 
 * @param triglyceride 
 * @param hepatitis 
 */
data class Exam (
    val SSN: kotlin.Int? = null,
    val lastName: kotlin.String? = null,
    val name: kotlin.String? = null,
    val date: kotlin.String? = null,
    val cholesterol: kotlin.Double? = null,
    val triglyceride: kotlin.Double? = null,
    val hepatitis: kotlin.Boolean? = null
) {

}
