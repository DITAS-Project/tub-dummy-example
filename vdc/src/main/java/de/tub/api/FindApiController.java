package de.tub.api;

import de.tub.model.Exams;
import org.joda.time.LocalDate;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T10:26:59.971Z")

@Controller
public class FindApiController implements FindApi {



    public ResponseEntity<Exams> findGet( @Min(0)@ApiParam(value = "minimal age") @RequestParam(value = "minage", required = false) Integer minage,
         @Min(0)@ApiParam(value = "maximal age ") @RequestParam(value = "maxage", required = false) Integer maxage,
        @ApiParam(value = "", allowableValues = "male, female") @RequestParam(value = "gender", required = false) String gender,
        @ApiParam(value = "name of the test to return", allowableValues = "cholesterol, triglyceride, hepatitis") @RequestParam(value = "type", required = false) String type,
        @ApiParam(value = "") @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @ApiParam(value = "") @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        // do some magic!
        return new ResponseEntity<Exams>(HttpStatus.OK);
    }

}
