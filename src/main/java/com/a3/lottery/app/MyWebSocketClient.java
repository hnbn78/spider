package com.a3.lottery.app;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        // for (Iterator<String> it = shake.iterateHttpFields(); it.hasNext();) {
        // String key = it.next();
        // System.out.println(key + ":" + shake.getFieldValue(key));
        // }
    }

    @Override
    public void onMessage(String paramString) {
        System.out.println("接收到消息：" + paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常" + e);

    }

    public static void main(String[] args) {
        try {
            String url = "wss://mainnet.eos.dfuse.io/v1/stream?token=eyJhbGciOiJLTVNFUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTkzMDIwMzksImp0aSI6ImY1MDdkMjI5LTBiM2ItNDUwNS1hMGE4LTRhOTMzMDY3ZTM5ZSIsImlhdCI6MTU1OTIxNTYzOSwiaXNzIjoiZGZ1c2UuaW8iLCJzdWIiOiJ1aWQ6MHN1dnU4MDBjOTUyZmUzY2ViYmQ0IiwiYWtpIjoiYmZjYjBlOWFkM2U0MGM1NWI3OGE2OGY5NzAxNTQ5YjU0ODg2YjVmNGExMDI3MDE0Nzk3ZTUwYjdhYzA2ZThmMyIsInRpZXIiOiJmcmVlLXYxIiwic3RibGsiOi0zNjAwLCJ2IjoxfQ.I8PHEYHg2IsXJB51WjRVfQILQq-DruXIJYALMpmNq1ct2sXgi3jaI2hc-FFEgmqf3D99G-tNi9PWiDA1tdRKTA";
            MyWebSocketClient client = new MyWebSocketClient(url);
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("还没有链接成功");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            String type = "{\"type\":\"get_head_info\",\"listen\":true,\"req_id\":\"1\"}";
            client.send(type);
            System.out.println("建立websocket连接");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}