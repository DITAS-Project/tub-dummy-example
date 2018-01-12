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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.tub.util.GenderConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.persistence.*;
/**
 * Patient
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")
@Entity
@Table(name = "PATIENT")
public class Patient   {
  @Id
  @JsonProperty("SSN")
  @Column(name="SSN")
  private Long SSN = null;

  @Column(name = "LASTNAME")
  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("name")
  @Column(name = "NAME")
  private String name = null;

  /**
   * Gets or Sets gender
   */
  public enum GenderEnum {
    MALE("m"),
    
    FEMALE("f"),
    
    UNDEFINED("NULL");

    private String value;

    GenderEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static GenderEnum fromValue(String text) {
      if(text == null) return null;

      for (GenderEnum b : GenderEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public String getCode() {
      return value;
    }

  }

  @JsonProperty("gender")
  @Basic
  @Convert( converter=GenderConverter.class )
  private GenderEnum gender = null;

  @JsonProperty("mail")
  @Column(name = "mail")
  private String mail = null;

  @JsonProperty("birthday")
  @Column(name = "birthday")
  private String birthday = null;

  public Patient SSN(Long SSN) {
    this.SSN = SSN;
    return this;
  }

   /**
   * Get SSN
   * @return SSN
  **/
  @ApiModelProperty(value = "")
  @JsonIgnore
  public Long getSSN() {
    return SSN;
  }

  public void setSSN(Long SSN) {
    this.SSN = SSN;
  }

  public Patient lastName(String lastName) {
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

  public Patient name(String name) {
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

  public Patient gender(GenderEnum gender) {
    this.gender = gender;
    return this;
  }

   /**
   * Get gender
   * @return gender
  **/
  @ApiModelProperty(value = "")


  public GenderEnum getGender() {
    return gender;
  }

  public void setGender(GenderEnum gender) {
    this.gender = gender;
  }

  public Patient mail(String mail) {
    this.mail = mail;
    return this;
  }

   /**
   * Get mail
   * @return mail
  **/
  @ApiModelProperty(value = "")


  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public Patient birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

   /**
   * Get birthday
   * @return birthday
  **/
  @ApiModelProperty(value = "")
  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Patient patient = (Patient) o;
    return Objects.equals(this.SSN, patient.SSN) &&
        Objects.equals(this.lastName, patient.lastName) &&
        Objects.equals(this.name, patient.name) &&
        Objects.equals(this.gender, patient.gender) &&
        Objects.equals(this.mail, patient.mail) &&
        Objects.equals(this.birthday, patient.birthday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(SSN, lastName, name, gender, mail, birthday);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Patient {\n");
    
    sb.append("    SSN: ").append(toIndentedString(SSN)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    mail: ").append(toIndentedString(mail)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
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

