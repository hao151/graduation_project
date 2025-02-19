package com.botzone.matchingsystem.service.impl;

import com.botzone.matchingsystem.service.MatchingService;
import com.botzone.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("addPlayer" + userId + " " + rating);
        matchingPool.addPlayer(userId, rating);
        return "add success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("removePlayer" + userId);
        matchingPool.removePlayer(userId);
        return "remove success";
    }
}
