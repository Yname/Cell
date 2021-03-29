import redis.clients.jedis.Connection;
import redis.clients.jedis.DefaultJedisSocketFactory;
import redis.clients.jedis.JedisSocketFactory;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.RedisOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class A extends Connection{

    public A(final String host, final int port, final boolean ssl,
                      SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
                      HostnameVerifier hostnameVerifier) {

        this(new DefaultJedisSocketFactory(host, port, 4000,
                4000, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
    }

    public A(final JedisSocketFactory jedisSocketFactory) {
        super(jedisSocketFactory);
    }



    public static void main(String[] args) throws IOException {
//        SecurityManager securityManager = new SecurityManager();
        Connection connection = new A("42.193.182.118",16739,false,null,null,null);

        connection.connect();


//        connection.sendCommand(Protocol.Command.RANDOMKEY, new byte[0][]);

//        byte[] binaryBulkReply = connection.getBinaryBulkReply();
//        System.out.println(binaryBulkReply);
//         public Connection(final String host, final int port, final boolean ssl,
//        SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
//                HostnameVerifier hostnameVerifier) {
//            this(new DefaultJedisSocketFactory(host, port, Protocol.DEFAULT_TIMEOUT,
//                    Protocol.DEFAULT_TIMEOUT, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
//        }

//        Socket socket = new Socket();
        // ->@wjw_add
//        socket.setReuseAddress(true);
//        socket.setKeepAlive(true); // Will monitor the TCP connection is
//        // valid
//        socket.setTcpNoDelay(true); // Socket buffer Whetherclosed, to
//        // ensure timely delivery of data
//        socket.setSoLinger(true, 0); // Control calls close () method,
        // the underlying socket is closed
        // immediately
        // <-@wjw_add

//        socket.connect(new InetSocketAddress("42.193.182.118", 16739), 10000);

    }
}
