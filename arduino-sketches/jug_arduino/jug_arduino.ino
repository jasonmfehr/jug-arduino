#include <SPI.h>
#include <Ethernet.h>

//TCP port the arduino will listen on for incoming connections
const unsigned int SERVER_PORT = 51420;

//constants for command numbers which are sent as the first byte
const byte CMD_ECHO = 0;
const byte CMD_SET_LED = 1;
const byte CMD_READ_TEMP = 2;
const byte CMD_SET_COLOR_LED = 3;

//constants for the number of data bytes that follow the initial command byte
const byte CMD_ECHO_DATA_BYTE_COUNT = 2;
const byte CMD_SET_LED_DATA_BYTE_COUNT = 2;
const byte CMD_READ_TEMP_DATA_BYTE_COUNT = 0;
const byte CMD_SET_COLOR_LED_DATA_BYTE_COUNT = 3;

//convenience array mapping the command numbers to their respective data byte counts
const byte COMMAND_TO_DATA_BYTE_COUNT_MAP[4] = {CMD_ECHO_DATA_BYTE_COUNT, CMD_SET_LED_DATA_BYTE_COUNT, CMD_READ_TEMP_DATA_BYTE_COUNT, CMD_SET_COLOR_LED_DATA_BYTE_COUNT};

//indicies of data bytes for the SET_LED command
const byte LED_NUMBER_INDEX = 0;
const byte LED_ONOFF_INDEX = 1;

//if the LED_ONOFF_INDEX data byte for the SET_LED command is this value, it means to turn off the LED
const byte LED_SET_OFF = 0;

//array mapping led numbers to the I/O pin they are on
const byte LED_PINS[4] = {2, 7, 8, 9};

//analog pin to which the temperature sensor is connected
const byte TEMP_INPUT_PIN = 0;
const float ANALOG_STEP = 0.0049;

//pin mappings for the PWM output pins that drive the multi-color LED
const byte RED_PIN = 3;
const byte GREEN_PIN = 5;
const byte BLUE_PIN = 6;

//mapping of colors to data byte array indicies
const byte RED_COLOR_INDEX = 0;
const byte GREEN_COLOR_INDEX = 1;
const byte BLUE_COLOR_INDEX = 2;

//number of miiliseconds to wait for the client to start sending bytes before timing out
unsigned long CLIENT_TIMEOUT = 10000;

//setup of the ethernet shield
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
byte ip[] = { 192, 168, 1, 7};
byte gateway[] = { 192, 168, 1, 1 };
byte subnet[] = { 255, 255, 255, 0 };

EthernetServer server(SERVER_PORT);

void setup() {
  Ethernet.begin(mac, ip, gateway, subnet);
  server.begin();
  
  Serial.begin(9600);
  Serial.print("\n\n------------------\nServer at ");
  Serial.print(Ethernet.localIP());
  Serial.print(":");
  Serial.println(SERVER_PORT);
  
  setupLEDs();
  setupMultiColorLED();
}

void loop() {
  EthernetClient client;
  byte commandByte;
  byte* dataBytes;
  byte dataBytesCount;
  
  //server.available() is not a blocking call, so we must check to see if a client has connected
  //but, client will be false until at least one byte is available for reading, thus the first 
  //client.read() command is guaranteed to return the first byte that was sent by the client
  client = server.available();
  if(client){
    Serial.println("\nclient connected");
    
    commandByte = client.read();
    Serial.print("got command byte: ");
    Serial.println(commandByte);
    
    //default to zero for unknown commands
    if(commandByte >= 0 && commandByte < (sizeof(COMMAND_TO_DATA_BYTE_COUNT_MAP) / sizeof(byte))){
      dataBytesCount = COMMAND_TO_DATA_BYTE_COUNT_MAP[commandByte];
      Serial.print("reading number of data bytes: ");
      Serial.println(dataBytesCount);
      dataBytes = readDataBytes(&client, dataBytesCount);
    }
    
    switch(commandByte){
      case CMD_ECHO:
        Serial.println("command ECHO");
        handleEchoCommand(&client, dataBytes);
        break;
      case CMD_SET_LED:
        Serial.println("command SET_LED");
        handleSetLedCommand(dataBytes);
        break;
      case CMD_READ_TEMP:
        Serial.println("command READ_TEMP");
        handleReadTempCommand(&client);
        break;
      case CMD_SET_COLOR_LED:
        Serial.println("command SET_COLOR_LED");
        handleSetColorLedCommand(dataBytes);
        break;
      default:
        Serial.print("unknown command: ");
        Serial.println(commandByte);
        break;    
    };

    client.stop();
    Serial.println("terminated client connection");
    delete dataBytes;
  }
}

void handleEchoCommand(EthernetClient* client, byte* data) {
  Serial.println("echoing bytes to client");
  client->write(data, CMD_ECHO_DATA_BYTE_COUNT);
  Serial.println("done echoing bytes to client");
}

void handleSetLedCommand(byte* data) {
  byte ledNumber = data[LED_NUMBER_INDEX];
  byte ledOnOrOff = data[LED_ONOFF_INDEX];
  
  Serial.print("setting led ");
  Serial.print(data[LED_NUMBER_INDEX]);
  Serial.print(" to value ");
  Serial.println(data[LED_ONOFF_INDEX]);
  
  if(ledOnOrOff == LED_SET_OFF){
    digitalWrite(LED_PINS[ledNumber], LOW);
  }else{
    digitalWrite(LED_PINS[ledNumber], HIGH);
  }
}

void handleReadTempCommand(EthernetClient* client) {
  byte temp;
  float readVoltage;
  
  //the voltage is returned as a number from 0 to 1023 with each number representing 0.0049 volts, 
  //multiply to get the actual voltage that is read
  readVoltage = analogRead(TEMP_INPUT_PIN) * ANALOG_STEP;
  Serial.print("read voltage: ");
  Serial.println(readVoltage);
  
  //a reading of 0.5 volts indicates 0 degrees celsius
  temp = (readVoltage - 0.5) * 100;
  
  Serial.print("read temperature of ");
  Serial.println(temp);
  
  client->write(temp);
}

void handleSetColorLedCommand(byte* data) {
  Serial.print("setting RGB LED color to ");
  Serial.print(data[RED_COLOR_INDEX]);
  Serial.print(",");
  Serial.print(data[GREEN_COLOR_INDEX]);
  Serial.print(",");
  Serial.println(data[BLUE_COLOR_INDEX]);

  analogWrite(RED_PIN, data[RED_COLOR_INDEX]);
  analogWrite(GREEN_PIN, data[GREEN_COLOR_INDEX]);
  analogWrite(BLUE_PIN, data[BLUE_COLOR_INDEX]);
}

/**
 * reads a specified number of bytes that are sent from the client and returns an array with those bytes
 * assumes the caller of this method knows how big the array will be
 *
 * client - pointer to the client connection object
 * numberOfDataBytes - count of bytes to read
 *
 * return - byte array created using the new command, this byte array 
 *          must be explicitly deleted or it will cause a memory leak
 */
byte* readDataBytes(EthernetClient* client, byte numberOfDataBytes) {
  unsigned long waitStartTime = millis();
  byte inputByte;
  byte bytesRead = 0;
  byte* dataByteArray = new byte[numberOfDataBytes];
  
  Serial.print("waiting on client to write number of data bytes: ");
  Serial.println(numberOfDataBytes);
  
  while(bytesRead < numberOfDataBytes && (millis() - waitStartTime < CLIENT_TIMEOUT)){
    //since client->read() is a non-blocking call, check to see if bytes are available for reading before attempting a read
    if(client->available()){
      inputByte = client->read();
      Serial.print("read byte: ");
      Serial.println(inputByte);
      dataByteArray[bytesRead++] = inputByte;
    }
  }
  
  if(millis() - waitStartTime >= CLIENT_TIMEOUT){
    Serial.println("error - timed out waiting for client");
  }
  
  return dataByteArray;
}

void setupLEDs() {
  //setup pins that the leds are connected to in output mode
  //and flash them briefly as a test
  for(byte i=0; i<sizeof(LED_PINS)/sizeof(byte); i++){
    pinMode(LED_PINS[i], OUTPUT);
    digitalWrite(LED_PINS[i], HIGH);
    delay(250);
    digitalWrite(LED_PINS[i], LOW);
    delay(250);
  }
}

void setupMultiColorLED() {
  analogWrite(RED_PIN, 0);
  analogWrite(GREEN_PIN, 0);
  analogWrite(BLUE_PIN, 0);
  
  flashColor(RED_PIN);
  flashColor(GREEN_PIN);
  flashColor(BLUE_PIN);
}

void flashColor(byte pin) {
  analogWrite(pin, 255);
  delay(500);
  analogWrite(pin, 0);
}

