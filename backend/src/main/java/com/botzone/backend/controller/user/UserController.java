package com.botzone.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.botzone.backend.mapper.UserMapper;
import com.botzone.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;
    @GetMapping("/user/all/")
    public List<User> getAll() {            //查看所有用户
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable int userId) {  //查看一个用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/user/add/{userId}/{username}/{password}")
    public String addUser(    //添加一个用户
            @PathVariable int userId,
            @PathVariable   String username,
            @PathVariable String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userId, username, encodedPassword);

        userMapper.insert(user);
        return "insert successfully";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {   //删除一个用户
        userMapper.deleteById(userId);
        return "delete successfully";
    }
}
