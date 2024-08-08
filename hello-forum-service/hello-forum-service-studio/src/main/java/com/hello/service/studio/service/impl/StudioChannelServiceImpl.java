package com.hello.service.studio.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.model.studio.pojos.StudioChannel;
import com.hello.service.studio.mapper.StudioChannelMapper;
import com.hello.service.studio.service.StudioChannelService;
import org.springframework.stereotype.Service;

@Service
public class StudioChannelServiceImpl extends ServiceImpl<StudioChannelMapper, StudioChannel> implements StudioChannelService {

}
