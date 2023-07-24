package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.traffic.domain.TrafficIntersection;
import com.ruoyi.traffic.mapper.TrafficIntersectionMapper;
import com.ruoyi.traffic.service.ITrafficIntersectionService;
import org.springframework.stereotype.Service;

/**
 * @classname: TrafficIntersectionServiceImpl
 * @author: ouyangdelong
 * @description: 路口服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service
public class TrafficIntersectionServiceImpl extends ServiceImpl<TrafficIntersectionMapper, TrafficIntersection>
        implements ITrafficIntersectionService {
}
