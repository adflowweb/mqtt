echo "usage :./getConnect.sh [phoneNumber]" 
echo "phoneNumber="$1
curl -XGET -H "Content-Type: application/json" -H "Accept-Version: 1.0.0" 127.0.0.1:8083/connect/$1?token=0123456789
