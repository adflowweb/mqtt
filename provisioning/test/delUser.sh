echo "usage: ./delUser.js [phoneNunber]"
echo "phoneNumber=" $1
curl -XDELETE  -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/user/$1?token=0123456789 | bunyan -o short
