echo "usage: ./postMqttbroker.sh [mqttbrokerName]"
echo "mqttbrokerName= " $1
curl -d @createmqttbroker.data -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/mqttbroker/$1 | bunyan -o short 
