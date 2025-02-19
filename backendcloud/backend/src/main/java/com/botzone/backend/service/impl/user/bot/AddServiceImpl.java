package com.botzone.backend.service.impl.user.bot;

import com.botzone.backend.mapper.BotMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.pojo.bot;
import com.botzone.backend.service.impl.utils.UserDetailsImpl;
import com.botzone.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

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

        Date now = new Date();
        bot bot = new bot(null, user.getId(), name, description, code, now, now);

        botMapper.insert(bot);
        result.put("error_message", "success");
        return result;
    }
}
