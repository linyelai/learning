package com.linyelai;

import com.linyelai.enums.ChatType;
import com.linyelai.enums.MsgType;
import com.linyelai.future.SendFuture;
import com.linyelai.request.MessageOuterClass;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

public class Client {

    private SocketChannel socketChannel;
    private SocketChannel lastSocketChannel;
    private SocketChannel oldSocketChannel;
    private volatile boolean connected = false;
    private Selector selector;
    private CustomDecoder customDecoder = new CustomDecoder();
    private CustomEncoder customEncoder = new CustomEncoder();

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));

    private static Map<Long, SendFuture> sendFutureMap = new ConcurrentHashMap<>();
    private static InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8888);
    private static volatile boolean login = false;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);

    private static long userId;
    private static Client client = new Client();


    public synchronized void connect() {


            while (true) {
                try {
                    System.out.println("準備鏈接");
                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    socketChannel.connect(inetSocketAddress);
                    connected = socketChannel.finishConnect();
                    lastSocketChannel = socketChannel;
                    oldSocketChannel = socketChannel;
                    if (connected) {
                        System.out.println("finished");
                        break;
                    } else {
                        System.out.println("unfinished");

                    }
                    Thread.sleep(5*10L);
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        Thread.sleep(5 * 10L);
                    }catch (Exception e1){
                        e.printStackTrace();
                    }
                }
            }



    }


    public void read() throws IOException, ClassNotFoundException {


        while (true) {

            try {


                    int n = selector.select();
                    if (n > 0) {
                        Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            if (selectionKey.isReadable()) {
                                try {
                                    read((SocketChannel) selectionKey.channel());
                                }catch (Exception e){
                                    iterator.remove();
                                    continue;
                                }
                            }
                            iterator.remove();
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void read(SocketChannel socketChannel) throws IOException, ClassNotFoundException {

        //读取数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int n = socketChannel.read(buffer);
        if (n < 0) {
            return;
        }
        ByteBuffer temp = ByteBuffer.allocate(n);
        temp.put(buffer.array(),0,n);
        List<MessageOuterClass.Message> list = customDecoder.decode(temp);
        if (list.size() == 0) {
            return;
        }
        for (MessageOuterClass.Message item : list) {

            long id = item.getId();
            long fromUserId = item.getFromUserId();
            if (fromUserId == userId) {
                // 发送ack
                System.out.println("ack: " + item.getContent());
                SendFuture sendFuture = sendFutureMap.get(id);
                if (sendFuture != null) {
                    sendFuture.set(item);
                } else {
                    System.out.println("send future is null");
                }
            } else {
                //收到通知
                System.out.println("聊天from user id: " + item.getFromUserId() + ",content: " + item.getContent());

            }
            sendFutureMap.remove(id);

        }

    }

    public void write(MessageOuterClass.Message message) throws IOException {
        ByteBuffer buffer = customEncoder.encode(message);
        socketChannel.write(buffer);

    }


    public void init() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {

        String str = args[0];
        userId = Long.parseLong(str);
        long toUserId = Long.parseLong(args[1]);
        client.init();
        client.connect();
        client.startRead();
        client.startLogin();
        client.startSingleChat(toUserId);
        client.startGroupChat();
        client.startHeartBeat();

    }

    public void startRead() {

        threadPoolExecutor.execute(() -> {

            try {
                client.read();
            } catch (Exception e) {
                e.printStackTrace();
                ReConnectService.connect(client, userId);
                threadPoolExecutor.execute(() -> {
                    client.login(userId);
                });


            }
        });

    }

    public void startLogin() {
        try {
            client.login(userId);
        } catch (Exception e) {
            e.printStackTrace();
            threadPoolExecutor.execute(() -> ReConnectService.connect(client, userId));
        }
    }

    public void startSingleChat(long toUserId) {

        // 發送個人聊天記錄
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {

            try {
                String content1 = "single chat ,msg: " + System.currentTimeMillis();
                client.send(toUserId, content1);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void startHeartBeat(){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            try {
                client.sendHeartBeat();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void sendHeartBeat() throws InterruptedException {


        long id = IdGenerator.nextId();
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                .setId(id).setMsgType(MsgType.HeartBeat.ordinal()).setChatType(ChatType.SINGLE.ordinal()).setContent("ping").setFromUserId(userId).build();
        SendFuture sendFuture = new SendFuture();
        sendFutureMap.put(id, sendFuture);
        try {
            client.write(message);
        } catch (Exception e) {
            e.printStackTrace();
            sendFuture.cancel(true);
            sendFutureMap.remove(id);
            client.connect();
            threadPoolExecutor.execute(() -> {
                client.login(userId);
            });
            return;

        }
        try {
            sendFuture.get(6, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            sendFutureMap.remove(id);
            client.connect();
            threadPoolExecutor.execute(() -> {
                client.login(userId);
            });
            return;
        }
        sendFutureMap.remove(id);


    }

    public void startGroupChat() {

        // 發送群組聊天記錄
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {

            try {
                String content1 = "group chat, msg: " + System.currentTimeMillis();
                client.send2Group(1L, content1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);

    }

    public void login(long userId) {


        while (true) {
            long id ;
            try {
                 id = IdGenerator.nextId();
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                    .setId(id).setMsgType(MsgType.Login.ordinal()).setFromUserId(userId).setContent("login").build();
            SendFuture sendFuture = new SendFuture();
            sendFutureMap.put(id, sendFuture);
            try {
                System.out.println("request login");
                client.write(message);
            } catch (Exception e) {
                e.printStackTrace();
                sendFuture.cancel(true);
                sendFutureMap.remove(id);
                client.connect();
               continue;
            }
            System.out.println("wait login responase");
            try {
                sendFuture.get();
            }catch (Exception e){

                e.printStackTrace();
            }
            sendFutureMap.remove(id);
            login = true;
            System.out.println(" finished login ");
            return;
        }

    }

    public void send(long toUserId, String msg) throws ExecutionException, InterruptedException, IOException, TimeoutException {

        System.out.println("single chat ,to userId: " + toUserId + " ,fromUserId: " + userId + ", msg: " + msg);
        if (!client.connected) {
            System.out.println("close");

        }
        long id = IdGenerator.nextId();
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                .setId(id).setMsgType(MsgType.Text.ordinal()).setToUserId(toUserId).setChatType(ChatType.SINGLE.ordinal()).setContent(msg).setFromUserId(userId).build();
        SendFuture sendFuture = new SendFuture();
        sendFutureMap.put(id, sendFuture);
        try {
            client.write(message);
        } catch (Exception e) {
            e.printStackTrace();
            sendFuture.cancel(true);
            sendFutureMap.remove(id);
            throw e;

        }
        try {
            sendFuture.get(6, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            sendFutureMap.remove(id);
            return;
        }
        sendFutureMap.remove(id);
    }

    public void send2Group(long groupId, String msg) throws InterruptedException, ExecutionException, IOException, TimeoutException {
        if (!client.connected) {
            System.out.println("close");

        }
        System.out.println("group chat ,to group: " + groupId + ", fromUserId: " + userId + ", msg: " + msg);
        long id = IdGenerator.nextId();
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                .setId(id).setMsgType(MsgType.Text.ordinal()).setToUserId(groupId).setChatType(ChatType.GROUP.ordinal()).setContent(msg).setFromUserId(userId).build();
        SendFuture sendFuture = new SendFuture();
        sendFutureMap.put(id, sendFuture);
        try {
            client.write(message);
        } catch (Exception e) {
            e.printStackTrace();
            sendFuture.cancel(true);
            sendFutureMap.remove(id);
            throw e;

        }
        try {
            sendFuture.get(6, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            sendFutureMap.remove(id);
            return;
        }
        sendFutureMap.remove(id);
    }

    public boolean isConnect() {
        return client.connected;
    }

    public void setLogin(boolean b) {
        this.login = b;
    }

    public boolean isLogin() {
        return this.login;
    }
}
