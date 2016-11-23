/**
 *
 * Property of IBM Korea, Copyrightⓒ. IBM Korea 2016 All Rights Reserved.
 *
 * @author: Choi Eui Shin
 *
 */
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <esp.h>
#include <ArduinoJson.h>

#define _DEBUG_         0

#if _DEBUG_ == 1
    #define BEGIN_DEBUG(a) Serial.begin(a)
    #define DBG_PRINT(a) Serial.print(a)
    #define DBG_PRINTLN(a) Serial.println(a)
#else
    #define BEGIN_DEBUG(a)
    #define DBG_PRINT(a)
    #define DBG_PRINTLN(a)
#endif

#define LEN_FIELD      20   // 필드 크기
#define LEN_SWITCH     4

/*
 * SOFT AP 정보
 */
#define SOFT_AP_SSID   "SOFT_AP"
#define SOFT_AP_PWD    "1234567890"

/*
 * DATA STRUCTURE
 */
struct ESP_SETUP
{
    char wifiSsid[LEN_FIELD];
    char wifiPwd[LEN_FIELD];
    char mqttId[LEN_FIELD*2];
    char mqttPwd[LEN_FIELD*2];
    char mqttIp[LEN_FIELD*3];
    int mqttPort;
    char clientId[LEN_FIELD*3];
};

struct RC_SWITCH
{
    short port;
    unsigned long lastTime;
    short buttonNumber;
};


#define PIN_IN_LED     1
#define PIN_SWITCH_1   0
#define PIN_SWITCH_2   2
#define PIN_SWITCH_3   3
#define PIN_SWITCH_4   1

/*
 * GLOBAL VARIABLE
 */
struct ESP_SETUP config;

ESP8266WebServer server(8282);

const char * TOPIC_REMOTE = "iot-2/evt/status/fmt/json";

boolean flagWifi = false;
boolean flagMqtt = false;

unsigned long wifiTry = 0;
unsigned long mqttTry = 0;

WiFiClient wifiClient;
PubSubClient * mqttClient;

struct RC_SWITCH inSw[LEN_SWITCH] = {
        {PIN_SWITCH_1, 0, 1},
        {PIN_SWITCH_2, 0, 2},
        {PIN_SWITCH_3, 0, 3},
        {PIN_SWITCH_4, 0, 4},
};
boolean initPort = false;

void handleRoot();
void handleNotFound();
void modeSoftAP();
void handleSetup();
void clearEeprom();
void writeEeprom(char *conf);
void readEeprom(char * conf);
void sendCommand(int button);
void checkNetwork();
void checkMQTT();
void mqttCallback(const MQTT::Publish& pub);

/*
 *  초기화
 */
void enterInit()
{
    clearEeprom();
    ESP.restart();
}

/*
 * 초기화
 */
void setup()
{
    BEGIN_DEBUG(115200);
    memset(&config, 0x00, sizeof(struct ESP_SETUP));

    readEeprom((char *)&config);

    boolean a = config.wifiSsid[0] <= 0 || config.wifiSsid[0] >= 127 ? true : false;
    boolean b = config.wifiPwd[0] <= 0 || config.wifiPwd[0] >= 127 ? true : false;

    if ( a == true || b == true )
    {
        modeSoftAP();
    }
    else {
        WiFi.mode(WIFI_STA);
    }
}

/*
 * 반복 수행
 */
void loop()
{
#if _DEBUG_ == 0
    if ( initPort == false )
    {
        initPort = true;

        for(int i = 0; i < LEN_SWITCH; i++)
        {
            pinMode(inSw[i].port, INPUT_PULLUP);
        }
    }
#endif
    
    checkNetwork();

    /*
     * 스위치 입력이 있는지 검사한다.
     */    
    for(int i = 0; i < LEN_SWITCH; i++)
    {
        if ( digitalRead(inSw[i].port) == LOW )
        {
            unsigned long t = millis();
            if ( t - inSw[i].lastTime >= 1000 )
            {
                sendCommand(inSw[i].buttonNumber);
                inSw[i].lastTime = millis();
            }
        }
    }
}

/*
 * subscribe
 */
void mqttCallback(const MQTT::Publish& pub)
{
    char buffer[300];
    int len = pub.payload_len() >= 299 ? 299 : pub.payload_len();
    memcpy(buffer, pub.payload(), len);
    buffer[len] = 0;

    DBG_PRINT(">> Topic: ");
    DBG_PRINTLN(pub.topic());
    DBG_PRINT(">> Payload: ");
    DBG_PRINTLN(buffer);

    StaticJsonBuffer<1024> jsonBuffer;
    JsonObject& root = jsonBuffer.parseObject(buffer);
    JsonObject& d = root["d"];

    const char * id = d["identity"];
    const char * cmd = d["action"];
    const char * data = d["data"];

    if ( strcmp(id, config.clientId) != 0 )
        return;

    if ( strcmp(cmd, "") == 0 )
    {
        if ( strcmp(data, "") == 0 ) {
        }
    }
}

/*
 * MQTT 연결을 체크한다.
 */
void checkMQTT()
{
    if ( mqttClient != NULL && mqttClient->connected() == false )
    {
        DBG_PRINTLN("MQTT Disconnected");
        flagMqtt = false;
        mqttTry = 0;
    }
}

/*
 * Wifi 연결 및 MQTT 연결을 유지한다.
 */
void checkNetwork()
{
     if ( flagWifi == false )
     {
       if ( wifiTry == 0 || millis() - wifiTry >= 20000UL ) // 20sec
       {
           DBG_PRINT("\r\n>> Connecting to ");
           DBG_PRINTLN(config.wifiSsid);
           WiFi.begin(config.wifiSsid, config.wifiPwd);
           wifiTry = millis();
       }

       if ( WiFi.status() == WL_CONNECTED )
       {
         flagWifi = true;
         DBG_PRINT(">>Connected, IP =  ");
         DBG_PRINTLN(WiFi.localIP());
       }
  }
  else {
    delay(1);
    switch(WiFi.status())
    {
        //case WL_CONNECT_FAILED:
        //case WL_CONNECTION_LOST:
        case WL_DISCONNECTED:
        DBG_PRINT(">>Disconnected : wifi ");
        flagWifi = false;
        flagMqtt = false;
        wifiTry = 0;
        mqttTry = 0;
        break;
    }
  }

  if ( flagWifi == true )
  {
    if ( mqttClient == NULL ) {
        mqttClient = new PubSubClient(wifiClient, config.mqttIp, config.mqttPort);
        mqttClient->set_callback(mqttCallback);
    }

    if ( flagMqtt == false )
    {
        if ( mqttTry == 0 || millis() - mqttTry >= 10000UL ) // 10sec
        {
            mqttTry = millis();

            MQTT::Connect conn(config.clientId);
            conn.set_keepalive(10);
            conn.set_auth(config.mqttId, config.mqttPwd);

            if ( mqttClient->connect(conn) )
            {
                flagMqtt = true;
                DBG_PRINTLN("Connected to MQTT broker");
            }
            else {
                DBG_PRINTLN("MQTT connect failed");
            }
        }
    }
    else {
        if ( mqttClient->connected() == true )
        {
            mqttClient->loop();
        }
        else {
            flagMqtt = false;
            mqttTry = 0;
        }
    }
  }
}

/*
 * 디바이스 상태를 전송한다.
 */
void sendCommand(int button)
{
    if ( flagMqtt == false )
        return;

    StaticJsonBuffer<300> jsonBuffer;
    JsonObject& d = jsonBuffer.createObject();
    JsonObject& payload = jsonBuffer.createObject();

    payload["dvid"] = config.clientId;
    payload["action"] = "IR_REMOTE";
    payload["irButton"] = button;

    d["d"] = payload;

    char buffer[300];
    memset(buffer, 0x00, sizeof(buffer));
    d.printTo(buffer, 299);

    if (mqttClient->publish(TOPIC_REMOTE, buffer)) {
        DBG_PRINTLN("Publish ok");
    }
    else {
       DBG_PRINTLN("Publish failed");
       checkMQTT();
    }
}

/*
 * WIFI 연결정보를 EEPROM에서 읽어들인다.
 */
void readEeprom(char * conf)
{
    EEPROM.begin(sizeof(struct ESP_SETUP));

    for(int i = 0; i < sizeof(struct ESP_SETUP); i++) {
        conf[i] =  EEPROM.read(i);
    }

    DBG_PRINT("\nSSID : ");
    DBG_PRINTLN(config.wifiSsid);
    DBG_PRINT("PWD : ");
    DBG_PRINTLN(config.wifiPwd);
    
    DBG_PRINT("MQTT ID : ");
    DBG_PRINTLN(config.mqttId);
    DBG_PRINT("MQTT PWD : ");
    DBG_PRINTLN(config.mqttPwd);
    DBG_PRINT("BROKER : ");
    DBG_PRINTLN(config.mqttIp);
    DBG_PRINT("PORT : ");
    DBG_PRINTLN(config.mqttPort);
    DBG_PRINT("ID : ");
    DBG_PRINTLN(config.clientId);    
}

/*
 * WIFI 연결정보를 EEPROM에서 저장한다.
 */
void writeEeprom(char *conf)
{
    EEPROM.begin(sizeof(struct ESP_SETUP));

    for(int i = 0; i < sizeof(struct ESP_SETUP); i++) {
        EEPROM.write(i, *conf);
        conf++;
    }

    EEPROM.commit();
}

/*
 * WIFI 연결정보가 저장된 EEPROM 영역을 지운다.
 */
void clearEeprom()
{
    EEPROM.begin(sizeof(struct ESP_SETUP));

    for(int i = 0; i < sizeof(struct ESP_SETUP); i++) {
        EEPROM.write(i, 0);
    }

    EEPROM.commit();
}

/*
 * WIFI 설정 정보를 저장한다.
 */
void handleSetup()
{
    String ssid = server.arg("ssid");
    String pwd = server.arg("pwd");
    String mserver = server.arg("server");
    String port = server.arg("port");
    String id = server.arg("id");
    String usrpwd = server.arg("usrpwd");
    String clsid = server.arg("clsid");

    DBG_PRINTLN("-[INPUT]-------------------------");
    DBG_PRINTLN(ssid.c_str());
    DBG_PRINTLN(pwd.c_str());
    DBG_PRINTLN(mserver.c_str());
    DBG_PRINTLN(port.c_str());
    DBG_PRINTLN(id.c_str());
    DBG_PRINTLN(usrpwd.c_str());
    DBG_PRINTLN(clsid.c_str());
    DBG_PRINTLN("--------------------------");

    if ( ssid.length() > 0 || pwd.length() > 0 )
    {
        if ( ssid.length() > 0 )
            ssid.getBytes((unsigned char *)config.wifiSsid, LEN_FIELD-1);

        if ( pwd.length() > 0 )
            pwd.getBytes((unsigned char *)config.wifiPwd, LEN_FIELD-1);

        id.getBytes((unsigned char *)config.mqttId, LEN_FIELD-1);
        usrpwd.getBytes((unsigned char *)config.mqttPwd, LEN_FIELD-1);
        mserver.getBytes((unsigned char *)config.mqttIp, LEN_FIELD-1);
        clsid.getBytes((unsigned char *)config.clientId, LEN_FIELD-1);
        config.mqttPort = atoi(port.c_str());

        writeEeprom((char *)&config);
    }

    server.send(200, "text/plain", "Success");

    delay(1000);
    ESP.restart();
}


/*
 *  SOFT AP
 */
void modeSoftAP()
{
  pinMode(PIN_IN_LED, OUTPUT);
  digitalWrite(PIN_IN_LED, LOW);
  
  WiFi.mode(WIFI_AP);
  
  DBG_PRINT("\nSoft AP = ");
  DBG_PRINT(SOFT_AP_SSID);
  DBG_PRINT("/");
  DBG_PRINTLN(SOFT_AP_PWD);
  
  WiFi.softAP(SOFT_AP_SSID, SOFT_AP_PWD);

  IPAddress ip = WiFi.softAPIP();

  server.on("/", handleRoot);

  server.on("/choies", handleSetup);

  server.onNotFound(handleNotFound);
   
  server.begin();
  
  DBG_PRINT("\nWeb Server IP = ");
  DBG_PRINT(ip[0]);
  DBG_PRINT(".");
  DBG_PRINT(ip[1]);
  DBG_PRINT(".");
  DBG_PRINT(ip[2]);
  DBG_PRINT(".");
  DBG_PRINTLN(ip[3]);
  
  while(true)
    server.handleClient();
}

/*
 * 
 */
void handleNotFound()
{
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET)?"GET":"POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i=0; i<server.args(); i++){
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
}

/*
 * 
 */
void handleRoot()
{
    char temp[700];
    snprintf(temp, 700,
"<html><title>SETUP</title>\
<body>\
 <form action='/choies'>\
  SSID:<br>\
  <input type='text' name='ssid'>\
  <br>\
  PASSWORD:<br>\
  <input type='text' name='pwd'>\
  <br>\
  MQTT SERVER:<br>\
  <input type='text' name='server'>\
  <br>\
  MQTT PORT:<br>\
  <input type='text' name='port'>\
  <br>\
  MQTT ID:<br>\
  <input type='text' name='id'>\
  <br>\
  MQTT PASSWORD:<br>\
  <input type='text' name='usrpwd'>\
  <br>\
  CLIENT ID:<br>\
  <input type='text' name='clsid'>\
  <br>\
  <br><br>\
  <input type='submit' value='SET'>\
 </form>\
</body>\
</html>");
    
  server.send(200, "text/html", temp);
}

