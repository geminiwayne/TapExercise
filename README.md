# Taps Exercise

[![Java](http://img.shields.io/badge/Java%20-green.svg)](https://www.java.com/en/download/)[![maven](http://img.shields.io/badge/maven%20-red.svg)](https://maven.apache.org)

## Description

  This project is a simple tap exercise. This project assumes the system only return tap on and tap off from start stop to end stop. For example, user tap on at stop1 and tap off at stop3 and there is no data about stop2. 

------
## Dependency

| package             |            method                 |
|---------------------|-----------------------------------|
| Java 1.8            | https://www.java.com/en/download/ |
| Maven               | https://maven.apache.org          |

------
## <1> Deploy

  1. Enter the ExerciseProject file

  2. To run <mvn install>( check maven version -> mvn version)

------
## <2> Run
   <1>If using the IDE, import the project and run tap.java(Before running the project, you need run CsvGenerator.java to generate fake data).
   <2>If not, Enter the folder and run javac xxx.java then java xxx.
------

## <3> Test and Clean up 

  1. mvn test
  2. mvn clean
