package de.tub.api;

import de.tub.model.Exam;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")

@Controller
public class ExamApiController implements ExamApi {


    public ResponseEntity<Exam> examSSNGet(@ApiParam(value = "unique ID of a patient", required = true) @PathVariable("SSN") Long SSN) {
        // do some magic!
        return new ResponseEntity<Exam>(HttpStatus.OK);
    }

    public ResponseEntity<Exam> examSSNTestGet(@ApiParam(value = "unique ID of a patient", required = true) @PathVariable("SSN") Long SSN,
                                               @ApiParam(value = "name of the test to return", required = true,
                                                         allowableValues = "{values=[cholesterol, triglyceride, hepatitis], enumVars=[{name=CHOLESTEROL, value=\"cholesterol\"}, {name=TRIGLYCERIDE, value=\"triglyceride\"}, {name=HEPATITIS, value=\"hepatitis\"}]}") @PathVariable("test") String test) {
        // do some magic!
        return new ResponseEntity<Exam>(HttpStatus.OK);
    }

}

