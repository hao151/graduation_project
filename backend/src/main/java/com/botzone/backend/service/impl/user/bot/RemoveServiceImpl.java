package com.botzone.backend.service.impl.user.bot;

import com.botzone.backend.mapper.BotMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.pojo.bot;
import com.botzone.backend.service.impl.utils.UserDetailsImpl;
import com.botzone.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        int bot_id = Integer.parseInt(data.get("bot_id"));

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        bot bot = botMapper.selectById(bot_id);

        Map<String, String> result = new HashMap<>();
        if (bot == null) {
            result.put("error_message", "Bot不存在");
            return result;
        }

        if (!bot.getUserId().equals(user.getId())){
            result.put("error_message", "没有权限删除该bot");
            return result;
        }

        botMapper.deleteById(bot_id);

        result.put("error_message", "success");

        return result;
    }
}
