package com.botzone.backend.service.impl.pk;

import com.botzone.backend.consumer.WebSocketServer;
import com.botzone.backend.consumer.utils.Game;
import com.botzone.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {

    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("Received BotMove" + userId + " " + direction);
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                        game.setNextStepA(direction);
                }else if (game.getPlayerB().getId().equals(userId)) {
                        game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move successfully";
    }
}
