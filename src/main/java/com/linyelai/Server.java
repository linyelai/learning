package com.linyelai;

import com.linyelai.enums.ChatType;
import com.linyelai.enums.MsgType;
import com.linyelai.request.MessageOuterClass;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {


   private static  ServerSocketChannel serverSocketChannel ;
   private static  Selector selector ;
   private static  CustomEncoder customEncoder = new CustomEncoder();

   private static Map<SocketChannel,CustomDecoder> decoderMap = new HashMap<>();

   private static Map<Long,List<ClientSession>> clientSessionMap = new HashMap<>();

   //
   // private static Map<Long,SocketChannel> socketChannelMap = new ConcurrentHashMap<>();
   private static Map<SocketChannel,Long>  channelUserIdMap = new ConcurrentHashMap<>();

   private static Map<Long, LinkedBlockingDeque> historyMap = new ConcurrentHashMap<>();

    public void start() throws IOException {

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
        serverSocketChannel.bind(inetSocketAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,serverSocketChannel);

        while (true){
            int n = selector.select();
            if(n>0){
               Set<SelectionKey> selectionKeySet =  selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    try {

                        if (selectionKey.isAcceptable()) {
                            System.out.println("有连接进来");
                            SocketChannel socketChannel = (SocketChannel) serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            //注册监听读,连接事件
                            socketChannel.register(selector, SelectionKey.OP_READ);

                        } else if (selectionKey.isReadable()) {
                            //读取数据
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            read(socketChannel);
                        }

                        iterator.remove();
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
            }
        }

    }

    public void read(SocketChannel socketChannel) throws IOException {

        //读取数据
        CustomDecoder customDecoder = getDecoder(socketChannel);
        try {
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
                System.out.println(item.getContent());
                int msgType = item.getMsgType();
                long fromUserId = item.getFromUserId();
                ByteBuffer buffer1 = customEncoder.encode(item);
                if(MsgType.Login.ordinal()==msgType){
                    //socketChannelMap.put(fromUserId,socketChannel);
                    socketChannel.write(buffer1);
                    channelUserIdMap.put(socketChannel,fromUserId);
                    //登記
                    ClientSession clientSession = new ClientSession(fromUserId,socketChannel );
                    List<ClientSession> clientSessionList = clientSessionMap.get(fromUserId);
                    if(clientSessionList==null){
                        clientSessionList=new ArrayList<>();
                    }
                    clientSessionList.add(clientSession);
                    clientSessionMap.put(fromUserId,clientSessionList);
                    //加入聊天室
                    RoomService roomService = new RoomService();
                    Room room = roomService.getRoomById(1L);
                    room.addMember(fromUserId);

                    continue;
                }
                if(MsgType.HeartBeat.ordinal()==msgType){
                    socketChannel.write(buffer1);
                    continue;
                }
                int chatType = item.getChatType();
                if(ChatType.SINGLE.ordinal()==chatType) {
                    long toUserId = item.getToUserId();
                    List<ClientSession> clientSessionList = clientSessionMap.get(toUserId);
                    if (!CollectionUtils.isEmpty(clientSessionList)) {
                        for (ClientSession session : clientSessionList) {

                            session.sendMsg(buffer1);
                            buffer1.flip();
                        }



                    }
                }else{
                    //獲取組號
                    Long groupId = item.getToUserId();
                    RoomService roomService = new RoomService();
                    Room room =  roomService.getRoomById(groupId);
                    List<Long> memberList = room.getMemberList();
                    for(Long memberId:memberList){
                        if(memberId==fromUserId){
                            continue;
                        }
                        List<ClientSession> clientSessionList = clientSessionMap.get(memberId);
                        if (!CollectionUtils.isEmpty(clientSessionList)) {
                            for (ClientSession session : clientSessionList) {

                                session.sendMsg(buffer1);
                                buffer1.flip();
                            }


                        }
                    }


                }
                socketChannel.write(buffer1);

            }
        }catch (Exception e){

            e.printStackTrace();
            decoderMap.remove(socketChannel);
            socketChannel.register(selector,0);

            // 刪除會話
            Long userId = channelUserIdMap.get(socketChannel);
            if(userId==null){
                return;
            }
           List<ClientSession> clientSessionList =  clientSessionMap.get(userId);
            if(!CollectionUtils.isEmpty(clientSessionList)){
                Iterator<ClientSession> iterator = clientSessionList.iterator();
                while(iterator.hasNext()){
                    ClientSession clientSession = iterator.next();
                    if(clientSession.getSocketChannel().equals(socketChannel)){
                        iterator.remove();
                    }
                }
            }
            channelUserIdMap.remove(socketChannel);

        }

    }

    public CustomDecoder getDecoder(SocketChannel socketChannel){

        CustomDecoder customDecoder = decoderMap.get(socketChannel);
        if(customDecoder==null){
            customDecoder = new CustomDecoder();
            decoderMap.put(socketChannel,customDecoder);
        }
        return customDecoder;
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.start();
    }
}
