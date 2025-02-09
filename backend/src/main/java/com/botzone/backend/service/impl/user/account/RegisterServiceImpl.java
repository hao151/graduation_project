package com.botzone.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.botzone.backend.mapper.UserMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null){
            map.put("error_messgae", "用户名不能为空");
            return map;
        }
        if (password == null || confirmPassword == null){
            map.put("error_message", "密码不能为空");
            return map;
        }
        username = username.trim();
        if (username.isEmpty()){
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if(password.isEmpty() || confirmPassword.isEmpty()){
            map.put("error_message", "密码不能为空");
            return map;
        }

        if (username.length() > 100){
            map.put("error_message", "用户名过长");
            return map;
        }

        if(password.length() > 100 || confirmPassword.length() > 100){
            map.put("error_message", "密码过长");
            return map;
        }

        if(!password.equals(confirmPassword)){
            map.put("error_message", "两次密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);

        if (!users.isEmpty()){
            map.put("error_message", "用户名已存在");
            return map;
        }
        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/126662_lg_935af1fed8.jpeg";
        User user = new User(null, username, encodePassword, photo);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }
}
