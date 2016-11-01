echo "phonenumber=" $1
curl -XGET -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/user/$1?token=0123456789 
