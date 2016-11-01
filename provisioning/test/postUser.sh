curl -d @createuser.data -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/user/test | bunyan -o short 
