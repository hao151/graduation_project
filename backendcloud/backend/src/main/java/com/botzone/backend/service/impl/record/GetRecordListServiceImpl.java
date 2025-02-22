package com.botzone.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.botzone.backend.mapper.RecordMapper;
import com.botzone.backend.mapper.UserMapper;
import com.botzone.backend.pojo.User;
import com.botzone.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.botzone.backend.pojo.Record;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject result = new JSONObject();

        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String winnerIs = "平局";
            if ("A".equals(record.getLoser())) winnerIs = "PlayerB获胜";
            else if ("B".equals(record.getLoser())) winnerIs = "PlayerA获胜";
            item.put("winner", winnerIs);
            item.put("record", record);
            items.add(item);
        }
        result.put("records", items);
        result.put("records_count", recordMapper.selectCount(null));
        return result;
    }
}
