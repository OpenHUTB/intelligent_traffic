package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.traffic.domain.TrafficPoint;
import com.ruoyi.traffic.mapper.TrafficPointMapper;
import com.ruoyi.traffic.service.ITrafficPointService;
import org.springframework.stereotype.Service;

/**
 * @classname: TrafficPointServiceImpl
 * @author: chengchangli
 * @description: 路网的点的服务实现类
 * @date: 2023/7/18
 * @version: v1.0
 **/
@Service
public class TrafficPointServiceImpl extends ServiceImpl<TrafficPointMapper, TrafficPoint> implements ITrafficPointService {
}
