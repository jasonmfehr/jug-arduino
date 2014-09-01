#include <SPI.h>
#include <Ethernet.h>

const unsigned int SERVER_PORT = 51420;

//constants for commands
const byte CMD_ECHO = 0;
const byte CMD_ECHO_DATA_BYTE_COUNT = 2;
const byte CMD_SET_LED = 1;
const byte CMD_SET_LED_DATA_BYTE_COUNT = 2;
const byte CMD_READ_TEMP = 2;
const byte CMD_READ_TEMP_DATA_BYTE_COUNT = 0;

//indicies of data bytes for the SET_LED command
const byte LED_NUMBER_INDEX = 0;
const byte LED_ONOFF_INDEX = 1;
const byte LED_SET_OFF = 0;

//array mapping led numbers to the pin they are on
const byte LED_PINS[4] = {2, 3, 5, 6};

//analog pin to which the temperature sensor is connected
const byte TEMP_INPUT_PIN = 0;
const float ANALOG_STEP = 0.0049;

//number of miiliseconds to wait for the client to start sending bytes
unsigned long CLIENT_TIMEOUT = 10000;

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
}

void loop() {
  EthernetClient client = server.available();
  byte commandByte;
  byte* dataBytes;
  
  if(client){
    Serial.println("\nclient connected");
    
    //wait for the command byte to come in
    commandByte = client.read();
    Serial.print("got command byte: ");
    Serial.println(commandByte);
    
    dataBytes = readDataBytes(&client, determineNumberOfDataBytes(commandByte));
    
    switch(commandByte){
      case CMD_ECHO:
        handleEchoCommand(&client, dataBytes);
        break;
      case CMD_SET_LED:
        handleSetLedCommand(dataBytes);
        break;
      case CMD_READ_TEMP:
        handleReadTempCommand(&client);
        break;
      default:
        Serial.print("Unknown command: ");
        Serial.println(commandByte);
        break;    
    };

    client.stop();
    Serial.println("terminated client connection");
    delete dataBytes;
  }
}

byte determineNumberOfDataBytes(byte commandByte){
  byte num;
  
  switch(commandByte){
    case CMD_ECHO:
      Serial.println("command ECHO");
      num = CMD_ECHO_DATA_BYTE_COUNT;
      break;
    case CMD_SET_LED:
      Serial.println("command SET_LED");
      num = CMD_SET_LED_DATA_BYTE_COUNT;
      break;
    case CMD_READ_TEMP:
      Serial.println("command READ_TEMP");
      num = CMD_READ_TEMP_DATA_BYTE_COUNT;
    default:
      Serial.print("Unknown command: ");
      Serial.println(commandByte);
      num = 0;
      break;    
  };
  
  Serial.print("number of data bytes: ");
  Serial.println(num);
  
  return num;
}

void handleSetLedCommand(byte* data) {  
  Serial.print("setting led ");
  Serial.print(data[LED_NUMBER_INDEX]);
  Serial.print(" to value ");
  Serial.println(data[LED_ONOFF_INDEX]);
  
  if(data[LED_ONOFF_INDEX] == LED_SET_OFF){
    digitalWrite(LED_PINS[data[LED_NUMBER_INDEX]], LOW);
  }else{
    digitalWrite(LED_PINS[data[LED_NUMBER_INDEX]], HIGH);
  }
}

void handleEchoCommand(EthernetClient* client, byte* data) {
  Serial.println("echoing bytes to client");
  client->write(data, CMD_ECHO_DATA_BYTE_COUNT);
  Serial.println("done echoing bytes to client");
}

void handleReadTempCommand(EthernetClient* client) {
  byte temp = readTemp();
  
  Serial.print("read temperature of ");
  Serial.println(temp);
  
  client->write(temp);
}

byte* readDataBytes(EthernetClient* client, byte numberOfDataBytes) {
  unsigned long waitStartTime = millis();
  byte inputByte;
  byte bytesRead = 0;
  byte* dataByteArray = new byte[numberOfDataBytes];
  
  Serial.print("waiting on client to write number of data bytes: ");
  Serial.println(numberOfDataBytes);
  
  while(bytesRead < numberOfDataBytes && (millis() - waitStartTime < CLIENT_TIMEOUT)){
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

byte readTemp() {
  //the voltage is returned as a number from 0 to 1023 with each number representing 0.0049 volts, 
  //multiply to get the actual voltage that is read
  float readVoltage = analogRead(TEMP_INPUT_PIN) * ANALOG_STEP;
  Serial.print("read voltage: ");
  Serial.println(readVoltage);
  
  //a reading of 0.005 volts indicates 0 degrees celsius
  return (readVoltage - 0.5) * 100;
}

