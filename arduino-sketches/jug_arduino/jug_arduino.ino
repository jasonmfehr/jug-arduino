#include <SPI.h>
#include <Ethernet.h>

#define LED_1_PIN 2;
#define LED_2_PIN 3;
#define LED_3_PIN 5;
#define LED_1_PIN 6;

const byte CMD_SET_LED = 1;
//indicies of data bytes for the SET_LED command
const int LED_NUMBER_INDEX = 1;
const int LED_ONOFF_INDEX = 2;
const byte LED_SET_OFF = 0;

//general command byte indicies
const int COMMAND_DATA_LEN = 5;
const int COMMAND_BYTE_INDEX = 0;

//array mapping led numbers to the pin they are on
const int LED_PINS[4] = {2, 3, 5, 6};

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
byte ip[] = { 192, 168, 1, 7};
byte gateway[] = { 192, 168, 1, 1 };
byte subnet[] = { 255, 255, 255, 0 };

EthernetServer server(51420);

void setup() {
  Ethernet.begin(mac, ip, gateway, subnet);
  server.begin();
  
  Serial.begin(9600);
  Serial.print("Server at '");
  Serial.print(Ethernet.localIP());
  
  //setup pins that the leds are connected to in output mode
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
  byte commandData[COMMAND_DATA_LEN];
  int bytesRead;
  
  if(client){
    Serial.println("client connected");
    bytesRead = 0;
    
    while(client.available() && bytesRead < 5){
      commandData[bytesRead] = client.read();
      Serial.print("read byte: ");
      Serial.println(commandData[bytesRead]);
      bytesRead++;
    }
    
    client.stop();
    Serial.println("terminated client connection");
    
    processData(commandData);
  }
}

void processData(byte commandData[]) {
  switch(commandData[COMMAND_BYTE_INDEX]){
    case CMD_SET_LED:
      handleSetLed(commandData);
      break;
    default:
      Serial.print("Unknown command: ");
      Serial.println(commandData[COMMAND_BYTE_INDEX]);
      break;
  };
}

void handleSetLed(byte commandData[]) {
  Serial.print("Setting led ");
  Serial.print(commandData[LED_NUMBER_INDEX]);
  Serial.print(" to value ");
  Serial.println(commandData[LED_ONOFF_INDEX]);
  
  if(commandData[LED_ONOFF_INDEX] == LED_SET_OFF){
    digitalWrite(LED_PINS[commandData[LED_NUMBER_INDEX]], LOW);
  }else{
    digitalWrite(LED_PINS[commandData[LED_NUMBER_INDEX]], HIGH);
  }

}
