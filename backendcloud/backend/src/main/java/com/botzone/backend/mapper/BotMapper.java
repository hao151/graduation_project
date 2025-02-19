package com.botzone.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.botzone.backend.pojo.bot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BotMapper extends BaseMapper<bot> {
}
