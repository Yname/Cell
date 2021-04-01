import org.Buf;
import org.CellData;
import orgP.Org;
import redis.clients.jedis.BinaryClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class kafad {
    public static void main(String[] args) throws InterruptedException {
        BinaryClient client = new BinaryClient("42.193.182.118",16379);

        Buf buf = Buf.getBuf();
        for (int i = 0; i < 10; i++) {
            ExecutorService service = buf.getService();
            int finalI = i;
            service.execute(() -> {
                CellData data = new CellData("11110"+ finalI,"123","1234");
                Map<byte[],byte[]> map = new HashMap<>();
                map.put("org".getBytes(),data.getOrg().getBytes());
                map.put("data".getBytes(),data.getData().getBytes());
                int a = Integer.parseInt(data.getDataHeader()) | 1;
                byte[] bytes = {(byte) a};
                synchronized (client){
                    client.exists(bytes);
                    Long integerReply = client.getIntegerReply();

                }
                synchronized (client) {
                    client.hmset(bytes, map);
                    String statusCodeReply = client.getStatusCodeReply();
                    System.out.println(statusCodeReply);
                }
            });
            service.shutdown();
        }


        Thread.currentThread().join();
    }
}
