# Readme

VDC mock up implementation for OSR use case of the DITAS project

offers a Rest Enabled application that combines two databases (cassandra and mysql)

### Usage:

1. define database connection using application.properties
2. to run use ```mvn spring-boot:run```

### API:
| Method | Path               | Details                      | Returns |
| :--- | :---| --- | --- |
| GET    | /patient/{ssn}     | ssn: number betwenn 0 and 50 | Patient |
| GET    | /exam/{ssn}        | ssn: number betwenn 0 and 50 | \[Exam,...\] |
| GET    | /find?...          | minAge=0-100, maxAge=0-100,startDate=yyyy/MM/dd,endDate=yyyy/MM/dd, gender=(m/f)|  \[Exam,...\] |

Patient:
```
{
  "SSN": 0,
  "lastName": "string",
  "name": "string",
  "gender": "male",
  "mail": "user@example.com",
  "birthday": "string"
}
```

Exam:
```
  {
    "SSN": 0,
    "lastName": "string",
    "name": "string",
    "date": "2017-11-28T16:36:57.713Z",
    "cholesterol": 0,
    "triglyceride": 0,
    "hepatitis": true
  }
```
