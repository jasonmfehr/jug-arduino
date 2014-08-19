#jug-arduino
## Introduction
This project is a simple demonstration for the September 2014 meetup of the [Illnois Java Users Group](http://www.meetup.com/IllinoisJUG/events/200692482/).  It is not intended to be a complete project that would be production ready.  The exception handling is especially lacking, the communication protocol between the server and the Arduino is very simplistic, and rendered webpage design, well, it does not exist.

Anyways, I hope you learn something from this project.  Enjoy, I always do!

## Communication Protocol Description
The Arduino board for which this project was built is the [Arduino Uno](http://arduino.cc/en/Main/arduinoBoardUno) which is woefully underpowered.  While a partial HTTP protocol could be implemented, it is very difficult.  See my [Arduino JSON Parser](https://github.com/jasonmfehr/arduino_json_parser).

The protocol consists of a five byte stream with the first byte indiciating the command to execute and the second through fifth byte for data.  This protocol is also request-response in that a connection is made, the five bytes are sent, optionally five bytes are sent back, and the connection is closed

This table lists all available commands and what each data byte contains.

Description | Command Byte 1 | Data Byte 2 | Data Byte 3 | Data Byte 4 | Data Byte 5
----------- | -------------- | ----------- | ----------- | ----------- |------------
Turn on/off LED | 1 | LED Number | 1 - indicates to turn LED on <br /> 0 - indicates to turn LED off | not used | not used
