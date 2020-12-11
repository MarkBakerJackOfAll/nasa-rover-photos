## Mark Baker - Example Mars Rover Picture Selector

Hello!  My name is Mark Baker, but people call me Baker at work.  Hopefully you see something you like here.  Enjoy.


## Other Documentation
[Development Log](DEVELOPMENT_LOG.md)
[Remaining Tasks](TODO.md)

## Requirements

* gradle installed (2.4.16 used)
* java installed (openjdk 11.0.9.1 used)
* permission to write to /tmp

## Build Instructions

* clone repo
* cd to project directory
* run 'gradle clean build'

## Run Instructions

* java -jar <local-path-to-project>/build/libs/nasa-rover-photos-1.0.0.jar <local-path-to-project>/src/main/resources/inputFile.txt

### Advanced Run Instructions
The above is the minimum to run against the file described in the project description.  You can pass any file path, relative or absolute as the first commandline argument.  Any additional arguments will be ignored.  You can also choose to pass in no arguments and the code will default to using /tmp/inputFile.txt to parse dates from.

The expected format of the provided file is newline separated dates.  Supported date formats are:
* yyyy-MM-dd
* MMM-d-yyyy
* MMM d, yyyy
* MM/dd/yy"

## Oddities

So there are a couple things in this project that I wouldn't do for a production project.  Mostly they were done to simplify and reduce the posibility of a failure when run locally.

* build directory checked in to git
    * This is mostly just in case something goes wrong and the user is unable to build the jar
* Dependencies packaged into produced jar
    * I am not a fan of Uber jars.  I believe that it would be better to have all the dependency jars on the classpath of the system.  This makes it easier to swap out a dependency version if a backwards compatible change has been made.  It also prevents exponential growth when you have many projects depending on one another
* Comments explaining thought process
    * Normally I would make comments about what the code is doing but not why I made the code work that way and what other options were.  But I thought that would be useful for the purposes of showing my work and understanding the things I think about as I write code.
