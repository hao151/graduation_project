package com.botzone.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.botzone.backend.consumer.utils.Game;
import com.botzone.backend.consumer.utils.JwtAuthentication;
import com.botzone.backend.mapper.BotMapper;
import com.botzone.backend.mapper.RecordMapper;
import com.botzone.backend.mapper.UserMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.pojo.bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private Session session = null;
    private User user;
    public Game game = null;
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    private static BotMapper botMapper;
    public static UserMapper userMapper;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {WebSocketServer.restTemplate = restTemplate;}

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    private void setBotMapper(BotMapper botMapper) {WebSocketServer.botMapper = botMapper;}

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected");

        Integer userID = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userID);

        if (this.user != null) {
            users.put(userID, this);
        }else{
            this.session.close();
        }

        System.out.println(users);

    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        bot botA = botMapper.selectById(aBotId);
        bot botB = botMapper.selectById(bBotId);

        Game game = new Game(13, 14, a.getId(), botA, b.getId(), botB);
        game.createMap();
        if (users.get(a.getId()) != null){
            users.get(a.getId()).game = game;
        }
        if (users.get(b.getId()) != null){
            users.get(b.getId()).game = game;
        }
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());

        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());

        respGame.put("map", game.getG());


        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null){
            users.get(a.getId()).sendMessage(respA.toJSONString());
        }


        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null){
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }

    private void startMatching(Integer botId){
        System.out.println("startMatching");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);

    }

    private void stopMatching(){
        System.out.println("stopMatching");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);

    }

    private void move(int direction){
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) {
                game.setNextStepA(direction);
            }
        }else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("received message");

        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        }else if ("stop-matching".equals(event)) {
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    public void sendMessage(String message) {
        // 后端给前端发消息
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
