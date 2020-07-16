# ICD10GM vocabulary generator for usage with OHDSI OMOP
This project generates a stacked ICD10GM vocabulary for the usage with OMOP.

It reads ICD10GM txt files provided by DIMDI (https://www.dimdi.de/dynamic/de/klassifikationen/downloads/) on a yearly cycle and process the concepts with code and description into the pattern corresponding to the concept table of OHDSI OMOP CDM v5.

## Getting Started - cloning the project

git clone https://github.com/inesreinecke/icd10gmVocabGenerator.git

## Prerequisites 

To build the project and create the executable jar (fat jar with dependencies) file, Maven has to be installed.

text files from DIMDI with ICD10GM codes and description by year are available in the project in directory allCodingsByYear/

## Build the jar file from soure code

cd "$DEV_PATH"/icd10gmgen
mvn package

## Run jar with expected arguments

java -jar target/icd10gmgen-0.0.1-SNAPSHOT.jar <currentYear> <inputFile> <database> <dbuser> <dbpassword> <table> <dbserverIp> <dbServerPort>  <encoding>

Sample for execution for year 2004:
java -jar target/icd10gmgen-0.0.1-SNAPSHOT.jar 2004 /"$ICD10GM_FILE_DIR"/2004.txt OHDSI postgres omop dev_cdm.concept 127.0.0.1 5432 UTF-8

## consolidated bashfiles to process multible years

You need to adapt the provieded scripts to fit your database setup and environment.

sh file available to build ICD10GM vocabulary stack from year 2000 to 2020 on icd10gmgen/icd10gm_generator2004TO2020.sh
sh file available to build ICD10WHO vocabulary stack for year 2013, 2016, 2019 icd10gmgen/icd10who_gen_2013_16_19.sh

## Authors
Ines Reinecke - Initial work - ProjectOwner
