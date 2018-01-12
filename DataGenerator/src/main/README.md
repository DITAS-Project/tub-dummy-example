# ReadMe

DataGenerator for the DITAS OSR mock up use case

### Usage:
1. Modify the three variabls of the DataGenerator class in order to affect the size of the resulting sqls files.
    ```   
        private static final int BATCH_SIZE = 500;
         private static final int PATIENT_SIZE = BATCH_SIZE*1000;
         private static final int EXAMS_PER_PATIENT_BOUND = 25;
    ```
    
2.  run the DataGenerator 