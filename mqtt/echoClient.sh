java -Xms512m -Xmx512m -XX:NewRatio=3 -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:echo_client_gc.log -cp ./target/classes:$HOME/.m2/repository/io/netty/netty-all/4.0.11.Final/netty-all-4.0.11.Final.jar kr.co.adflow.netty.EchoClient 127.0.0.1 3883