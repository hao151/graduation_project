package com.botzone.backend.controller.user.bot;

import com.botzone.backend.pojo.bot;
import com.botzone.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {
    @Autowired
    private GetListService getListService;

    @GetMapping("/user/bot/getlist/")
    public List<bot> getlist(){
        return getListService.getList();
    }
}
