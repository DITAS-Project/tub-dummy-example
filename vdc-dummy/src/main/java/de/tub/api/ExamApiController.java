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

package de.tub.api;

import de.tub.model.Exam;
import de.tub.model.Exams;
import de.tub.services.ExamService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")

@Controller
public class ExamApiController implements ExamApi {


    private ExamService examService;

    @Autowired
    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    public ResponseEntity<Exams> examSSNGet(
            @ApiParam(value = "unique ID of a patient", required = true) @PathVariable("SSN") Long SSN) {
        // do some magic!
        return ResponseEntity.ok(new Exams(examService.getExamBySSN(SSN)));
    }

    public ResponseEntity<Exams> examSSNTestGet(@ApiParam(value = "unique ID of a patient", required = true) @PathVariable("SSN") Long SSN,
                                               @ApiParam(value = "name of the test to return", required = true,
                                                         allowableValues = "{values=[cholesterol, triglyceride, hepatitis], enumVars=[{name=CHOLESTEROL, value=\"cholesterol\"}, {name=TRIGLYCERIDE, value=\"triglyceride\"}, {name=HEPATITIS, value=\"hepatitis\"}]}") @PathVariable("test") String test) {
        // do some magic!
        return ResponseEntity.ok(new Exams(examService.getExamBy(SSN,test)));
    }

}

