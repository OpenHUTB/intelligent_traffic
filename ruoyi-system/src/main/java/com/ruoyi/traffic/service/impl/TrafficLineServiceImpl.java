package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.traffic.domain.TrafficLine;
import com.ruoyi.traffic.mapper.TrafficLineMapper;
import com.ruoyi.traffic.service.ITrafficLineService;
import org.springframework.stereotype.Service;

/**
 * @classname: TrafficLineServiceImpl
 * @author: chengchangli
 * @description: 路网的线的服务实现类
 * @date: 2023/7/19
 * @version: v1.0
 **/
@Service
public class TrafficLineServiceImpl extends ServiceImpl<TrafficLineMapper, TrafficLine> implements ITrafficLineService {
}
