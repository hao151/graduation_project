package com.botzone.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try{
            boolean alreadyExists = false;
            for (Player player : players) {
                if (player.getUserId().equals(userId) && player.getBotId().equals(botId)) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                players.add(new Player(userId, rating, botId, 0));
            }
        }finally {
            lock.unlock();
        }
    }
    public void removePlayer(Integer userId) {
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;

        }finally {
            lock.unlock();
        }

    }

    private void increaseTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private void matchPlayers() {
        System.out.println("matching: " + players.toString());
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if (checkMatched(a, b)){
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) newPlayers.add(players.get(i));
        }
        players = newPlayers;
    }

    private boolean checkMatched(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTimeDelta = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return 10 * waitingTimeDelta >= ratingDelta;

    }

    private void sendResult(Player a, Player b) {
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }

            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }


    }
}
