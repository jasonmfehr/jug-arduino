#jug-arduino
## Introduction
This project is a simple demonstration for the September 2014 meetup of the [Illnois Java Users Group](http://www.meetup.com/IllinoisJUG/events/200692482/).  It is not intended to be a complete project that would be production ready.  The exception handling is especially lacking, the communication protocol between the server and the remote board is very simplistic and could cause issues with multiple threads all attempting to contact the remote board at the same time, etc.

Anyways, I hope you learn something from this project.  Enjoy, I always do!

## Project Setup
This app is a demonstration of a few different ways an Internet of Things application could be implemented.  The app is built to talk to a remote single board computer such as an Arduino that handles the input/output of the sensors and display components (such as LEDs).  It has only been tested with an Arduino board.

## Communication Protocol Description
### Overview
The Arduino board for which this project was built is the [Arduino Uno](http://arduino.cc/en/Main/arduinoBoardUno) which is woefully underpowered.  While a partial HTTP protocol could be implemented, it is very difficult.  See my [Arduino JSON Parser](https://github.com/jasonmfehr/arduino_json_parser).

The communication protocol is request-response where a connection is established, a stream of bytes is written to the remote board, and a steam of bytes is read from the remote board.  Depending on the command being executed, these byte streams could actually contain zero bytes.

### Request
After the connection is established, the remote board is sent a single byte that contains the command number.  Any input data bytes for that command are then sent to the remote board.  The number of data bytes is not indicated on the byte stream, so both the server and remote board have to know how many input data bytes will be sent.

### Response
The remote board does its processing and then writes back the output data bytes.  As with the request, the number of output data bytes is not specified within the byte stream.  Both the server and remote board have to know how many data bytes will be coming back.

### Commands
#### ECHO
Item | Explanation
---- | -----------
**Name** | ECHO
**Number** | 0
**Description** | serves as a simple ping type test that can be used to ensure the remote board is communicating correctly
**Input Bytes Length** | 2
**Input Byte 0** | any arbitrary data
**Input Byte 1** | any arbitrary data
**Output Bytes Length** | 2
**Output Byte 0** | the exact value provided in input byte 0
**Output Byte 1** | the exact value provided in input byte 1

#### SET LED
Item | Explanation
---- | -----------
**Name** | SET_LED
**Number** | 1
**Description** | sets one of the four LEDs to either on or off
**Input Bytes Length** | 2
**Input Byte 0** | number of the LED who’s state will be set
**Input Byte 1** | state of the LED, 0 to turn the LED off and 1 to turn the LED on
**Output Bytes Length** | 0

#### GET TEMPERATURE
Item | Explanation
---- | -----------
**Name** | GET_TEMP
**Number** | 2
**Description** | reads the air temperature around the remote board
**Input Bytes Length** | 0
**Output Bytes Length** | 1
**Output Byte 0** | the temperature in degrees celsius

#### SET COLOR LED
Item | Explanation
---- | -----------
**Name** | SET_COLOR_LED
**Number** | 3
**Description** | sets the red green blue values of the multi-color LED
**Input Bytes Length** | 3
**Input Byte 0** | value of the red LED
**Input Byte 1** | value of the green LED
**Input Byte 2** | value of the blue LED
**Output Bytes Length** | 0
