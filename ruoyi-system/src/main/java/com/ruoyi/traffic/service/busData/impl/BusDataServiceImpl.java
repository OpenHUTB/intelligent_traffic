package com.ruoyi.traffic.service.busData.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.busData.BusData;
import com.ruoyi.traffic.mapper.busData.BusDataMapper;
import com.ruoyi.traffic.service.busData.BusDataService;
import com.ruoyi.traffic.vo.BusDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusDataServiceImpl extends ServiceImpl<BusDataMapper, BusData> implements BusDataService {

    @Autowired
    private BusDataMapper busDataMapper;

    @Override
    public List<BusDataVo> getBusGps(String busNumber) {
        LambdaQueryWrapper<BusData> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(busNumber) && StringUtils.isNotBlank(busNumber)) {
            queryWrapper.select(BusData::getLongitude, BusData::getLatitude)
                    .eq(BusData::getBusNumber, busNumber);
        }
        List<BusData> busData = busDataMapper.selectList(queryWrapper);
        List<BusDataVo> busDataVoList = new ArrayList<>();
        // 每次加1的话数据太多了，绘制出来的路线太密了
        for (int i = 0; i < busData.size(); i += 500) {
            BusData data = busData.get(i);
            double longitude = Double.parseDouble(data.getLongitude()) / 1000;
            double latitude = Double.parseDouble(data.getLatitude()) / 1000;
            BusDataVo busDataVo = new BusDataVo(longitude, latitude);
            busDataVoList.add(busDataVo);
        }
        return busDataVoList;
    }
}
