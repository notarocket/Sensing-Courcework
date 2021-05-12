// This #include statement was automatically added by the Particle IDE.
#include <Keypad_Particle.h>

const char* serviceUuid = "a629a60a-d1c2-4f05-ad60-a8240c73a6ee";
const char* pcStatUuid = "2d2fa4ac-b22b-11eb-8529-0242ac130003";
const char* onOffUuid = "39df25ce-b22b-11eb-8529-0242ac130003";
char code [3];
int x = 0;
bool pcOn = false;

    BleCharacteristic pcStatCharacteristic("pcStat", BleCharacteristicProperty::READ, pcStatUuid, serviceUuid);
    BleCharacteristic onOffCharacteristic("onOff", BleCharacteristicProperty::WRITE_WO_RSP, onOffUuid, serviceUuid, onDataReceived, NULL);
    
    
    // Used https://www.ardumotive.com/how-to-use-a-keypad-en.html for keymap
    const byte ROWS = 4; //four rows
    const byte COLS = 3; //three columns
    char keys[ROWS][COLS] = {
    {'1','2','3'},
    {'4','5','6'},
    {'7','8','9'},
    {'*','0','#'}
    };
        
    byte rowPins[ROWS] = {8, 7, 6, 5}; //connect to the row pinouts of the keypad
    byte colPins[COLS] = {4, 3, 2}; //connect to the column pinouts of the keypad
    
    

Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS);


void setup(){
    
    Serial.begin(9600);
    //Add ble characterisitcs
    BLE.addCharacteristic(pcStatCharacteristic);
    BLE.addCharacteristic(onOffCharacteristic);

    
    pcStatCharacteristic.setValue("test");
    int storedCode = 1234;
    BleUuid pcService(serviceUuid);
    BleAdvertisingData advData;
    advData.appendServiceUUID(pcService);
    BLE.advertise(&advData);
    
    pinMode(D1, OUTPUT);


}

static void onDataReceived(const uint8_t* data, size_t len, const BlePeerDevice& peer, void* context) {
    Serial.println('D');
    
    //Converts incomming data to char format for comparison
    char* dataChar = (char*) data;
    char dataChar2[3] ;
    strcpy(dataChar2, dataChar);
    Serial.println((char*) dataChar2);
    Serial.println((char*) code);
    //Used to compare incomming code and stored code. Structured like this to avoid broken characters at end of the code array
    if(dataChar2[0] == code[0] and dataChar2[1] == code[1] and dataChar2[2] == code[2] and dataChar2[3] == code[3]){
        
        
        //Used to turn pc on/off
        if(pcOn == true){
            digitalWrite(D1, LOW);
            pcOn = false;
            pcStatCharacteristic.setValue('o');
        }else if(pcOn == false){
            digitalWrite(D1, HIGH);
            pcOn = true;
            pcStatCharacteristic.setValue('y');
        }
        
        
        delay(1200);
        
        
    }else{
        pcStatCharacteristic.setValue('c');
    }
    
}

void loop(){
    //Used to enter code
    char key = keypad.getKey();
    if(key != NO_KEY and x<4 and key != '*' and key != '#'){
        code[x] = key;
        int c = x+1;
        x = c;
        Serial.println(key);
        Serial.println("xyz ");
        Serial.print(x);
    }
    //Used to wipe code
    if(key == '*'){
      x=0;
      std::fill_n(code, 4, 0);
    }
    

  

    
}