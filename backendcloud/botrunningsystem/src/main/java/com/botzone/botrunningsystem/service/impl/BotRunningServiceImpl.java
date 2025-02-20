package com.botzone.botrunningsystem.service.impl;


import com.botzone.botrunningsystem.service.BotRunningService;
import com.botzone.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("addBot" + userId + " " + botCode + " " + input);
        botPool.addBot(userId, botCode, input);
        return "add Bot success!";
    }
}
