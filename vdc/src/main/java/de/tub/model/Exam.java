package de.tub.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Exam
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")

public class Exam   {
  @JsonProperty("SSN")
  private Long SSN = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("date")
  private DateTime date = null;

  @JsonProperty("cholesterol")
  private Double cholesterol = null;

  @JsonProperty("triglyceride")
  private Double triglyceride = null;

  @JsonProperty("hepatitis")
  private Boolean hepatitis = null;

  public Exam SSN(Long SSN) {
    this.SSN = SSN;
    return this;
  }

   /**
   * Get SSN
   * @return SSN
  **/
  @ApiModelProperty(value = "")


  public Long getSSN() {
    return SSN;
  }

  public void setSSN(Long SSN) {
    this.SSN = SSN;
  }

  public Exam lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Get lastName
   * @return lastName
  **/
  @ApiModelProperty(value = "")


  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Exam name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Exam date(DateTime date) {
    this.date = date;
    return this;
  }

   /**
   * Get date
   * @return date
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DateTime getDate() {
    return date;
  }

  public void setDate(DateTime date) {
    this.date = date;
  }

  public Exam cholesterol(Double cholesterol) {
    this.cholesterol = cholesterol;
    return this;
  }

   /**
   * Get cholesterol
   * @return cholesterol
  **/
  @ApiModelProperty(value = "")


  public Double getCholesterol() {
    return cholesterol;
  }

  public void setCholesterol(Double cholesterol) {
    this.cholesterol = cholesterol;
  }

  public Exam triglyceride(Double triglyceride) {
    this.triglyceride = triglyceride;
    return this;
  }

   /**
   * Get triglyceride
   * @return triglyceride
  **/
  @ApiModelProperty(value = "")


  public Double getTriglyceride() {
    return triglyceride;
  }

  public void setTriglyceride(Double triglyceride) {
    this.triglyceride = triglyceride;
  }

  public Exam hepatitis(Boolean hepatitis) {
    this.hepatitis = hepatitis;
    return this;
  }

   /**
   * Get hepatitis
   * @return hepatitis
  **/
  @ApiModelProperty(value = "")


  public Boolean getHepatitis() {
    return hepatitis;
  }

  public void setHepatitis(Boolean hepatitis) {
    this.hepatitis = hepatitis;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Exam exam = (Exam) o;
    return Objects.equals(this.SSN, exam.SSN) &&
        Objects.equals(this.lastName, exam.lastName) &&
        Objects.equals(this.name, exam.name) &&
        Objects.equals(this.date, exam.date) &&
        Objects.equals(this.cholesterol, exam.cholesterol) &&
        Objects.equals(this.triglyceride, exam.triglyceride) &&
        Objects.equals(this.hepatitis, exam.hepatitis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(SSN, lastName, name, date, cholesterol, triglyceride, hepatitis);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Exam {\n");
    
    sb.append("    SSN: ").append(toIndentedString(SSN)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    cholesterol: ").append(toIndentedString(cholesterol)).append("\n");
    sb.append("    triglyceride: ").append(toIndentedString(triglyceride)).append("\n");
    sb.append("    hepatitis: ").append(toIndentedString(hepatitis)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

