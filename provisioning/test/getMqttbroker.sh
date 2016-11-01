echo $1
curl -XGET -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/mqttbroker/$1 | bunyan -o short
