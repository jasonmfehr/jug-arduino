#include <SPI.h>
#include <Ethernet.h>

#define LED_1_PIN 2;
#define LED_2_PIN 3;
#define LED_3_PIN 5;
#define LED_1_PIN 6;

const unsigned int SERVER_PORT = 51420;

//constants for commands
const byte CMD_SET_LED = 1;
const byte CMD_SET_LED_DATA_BYTE_COUNT = 2;

//indicies of data bytes for the SET_LED command
const int LED_NUMBER_INDEX = 0;
const int LED_ONOFF_INDEX = 1;
const byte LED_SET_OFF = 0;

//number of miiliseconds to wait for the client to start sending bytes
unsigned long CLIENT_TIMEOUT = 10000;

//array mapping led numbers to the pin they are on
const int LED_PINS[4] = {2, 3, 5, 6};

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
  
  //setup pins that the leds are connected to in output mode
  //and flash then briefly as a test
  for(int i=0; i<sizeof(LED_PINS)/sizeof(int); i++){
    pinMode(LED_PINS[i], OUTPUT);
    digitalWrite(LED_PINS[i], HIGH);
    delay(250);
    digitalWrite(LED_PINS[i], LOW);
    delay(250);
  }
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
    
    dataBytes = readDataBytes(client, determineNumberOfDataBytes(commandByte));
    
    switch(commandByte){
      case CMD_SET_LED:
        handleSetLedCommand(dataBytes);
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

int determineNumberOfDataBytes(byte commandByte){
  int num;
  
  switch(commandByte){
    case CMD_SET_LED:
      Serial.println("command SET_LED");
      num = CMD_SET_LED_DATA_BYTE_COUNT;
      break;
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
  Serial.print("Setting led ");
  Serial.print(data[LED_NUMBER_INDEX]);
  Serial.print(" to value ");
  Serial.println(data[LED_ONOFF_INDEX]);
  
  if(data[LED_ONOFF_INDEX] == LED_SET_OFF){
    digitalWrite(LED_PINS[data[LED_NUMBER_INDEX]], LOW);
  }else{
    digitalWrite(LED_PINS[data[LED_NUMBER_INDEX]], HIGH);
  }
}

byte* readDataBytes(EthernetClient& client, int numberOfDataBytes) {
  unsigned long waitStartTime = millis();
  byte inputByte;
  int bytesRead = 0;
  byte* dataByteArray = new byte[numberOfDataBytes];
  
  Serial.print("waiting on client to write number of data bytes: ");
  Serial.println(numberOfDataBytes);
  
  while(bytesRead < numberOfDataBytes && (millis() - waitStartTime < CLIENT_TIMEOUT)){
    if(client.available()){
      inputByte = client.read();
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

