#!/bin/sh
#this program runs all java implementation to stack ICD10GM vocabulary into OHDSI OMOP concept table format
#important information: file encoding has been changed from ISO-8859-1 to UTF-8 starting with year 2009
#configuration needed for usage to get correct coding input data and executable file
codingDIR="../allCodingsByYear_WHO/"
impl="target/icd10gmgen-0.0.1-SNAPSHOT.jar"


java -jar $impl 2013 $codingDIR/2013.txt OHDSI postgres omop dev_cdm.conceptwho 127.0.0.1 5432 UTF-8
java -jar $impl 2016 $codingDIR/2016.txt OHDSI postgres omop dev_cdm.conceptwho 127.0.0.1 5432 UTF-8
java -jar $impl 2019 $codingDIR/2019.txt OHDSI postgres omop dev_cdm.conceptwho 127.0.0.1 5432 UTF-8