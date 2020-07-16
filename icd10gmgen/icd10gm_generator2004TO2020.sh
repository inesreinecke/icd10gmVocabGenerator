#!/bin/sh
#this program runs all java implementation to stack ICD10GM vocabulary into OHDSI OMOP concept table format
#important information: file encoding has been changed from ISO-8859-1 to UTF-8 starting with year 2009
#configuration needed for usage to get correct coding input data and executable file
codingDIR="../allCodingsByYear/"
impl="target/icd10gmgen-0.0.1-SNAPSHOT.jar"

java -jar $impl 2004 $codingDIR/2004.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 ISO-8859-1
java -jar $impl 2005 $codingDIR/2005.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 ISO-8859-1
java -jar $impl 2006 $codingDIR/2006.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 ISO-8859-1
java -jar $impl 2007 $codingDIR/2007.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 ISO-8859-1
java -jar $impl 2008 $codingDIR/2008.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 ISO-8859-1
java -jar $impl 2009 $codingDIR/2009.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2010 $codingDIR/2010.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2011 $codingDIR/2011.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2012 $codingDIR/2012.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2013 $codingDIR/2013.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2014 $codingDIR/2014.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2015 $codingDIR/2015.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2016 $codingDIR/2016.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2017 $codingDIR/2017.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2018 $codingDIR/2018.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2019 $codingDIR/2019.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
java -jar $impl 2020 $codingDIR/2020.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8
