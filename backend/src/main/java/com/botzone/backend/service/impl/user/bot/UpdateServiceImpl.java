package com.botzone.backend.service.impl.user.bot;

import com.botzone.backend.mapper.BotMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.pojo.bot;
import com.botzone.backend.service.impl.utils.UserDetailsImpl;
import com.botzone.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

        String name = data.get("name");
        String description = data.get("description");
        String code = data.get("code");


        Map<String, String> result = new HashMap<>();
        if (name == null || name.isEmpty()) {
            result.put("error_message", "bot名不能为空");
            return result;
        }

        if (name.length() > 100){
            result.put("error_message", "bot名过长");
            return result;
        }

        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么都没有写。";
        }

        if (description.length() > 1000){
            result.put("error_message", "bot简介过长");
            return result;
        }

        if (code == null || code.isEmpty()) {
            result.put("error_message", "不可上传空代码");
            return result;
        }

        if (code.length() > 10000){
            result.put("error_message", "代码过长,请优化代码。");
            return result;
        }

        bot old_bot = botMapper.selectById(bot_id);
        if (old_bot == null) {
            result.put("error_message", "bot不存在");
            return result;
        }

        if (!old_bot.getUserId().equals(user.getId())) {
            result.put("error_message", "没有权限修改该bot");
            return result;
        }

        bot new_bot = new bot(
                old_bot.getId(),
                user.getId(),
                name,
                description,
                code,
                old_bot.getRating(),
                old_bot.getCreateTime(),
                new Date()
        );

        botMapper.updateById(new_bot);
        result.put("error_message", "success");
        return result;

    }
}
