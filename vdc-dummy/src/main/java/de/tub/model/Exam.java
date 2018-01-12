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

package de.tub.model;

import java.util.Objects;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tub.util.JodaTimeCodec;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import javax.validation.Valid;

/**
 * Exam
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")

@Table(name = "exams", keyspace = "osr",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Exam   {

  @PartitionKey(0)
  @JsonProperty("SSN")
  @Column(name = "ssn")
  private Integer SSN = null;

  @JsonProperty("lastName")
  @Column(name = "lastname")
  private String lastName = null;

  @JsonProperty("name")
  @Column(name = "name")
  private String name = null;

  @PartitionKey(1)
  @JsonProperty("date")
  @Column(name = "date", codec = JodaTimeCodec.class)
  private DateTime date = null;

  @JsonProperty("cholesterol")
  @Column(name = "cholesterol")
  private Double cholesterol = null;

  @JsonProperty("triglyceride")
  @Column(name = "triglyceride")
  private Double triglyceride = null;

  @JsonProperty("hepatitis")
  @Column(name = "hepatitis")
  private Boolean hepatitis = null;

  public Exam SSN(Integer SSN) {
    this.SSN = SSN;
    return this;
  }

   /**
   * Get SSN
   * @return SSN
  **/
  @ApiModelProperty(value = "")
  @JsonIgnore
  public Integer getSSN() {
    return SSN;
  }


  public void setSSN(Integer SSN) {
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

