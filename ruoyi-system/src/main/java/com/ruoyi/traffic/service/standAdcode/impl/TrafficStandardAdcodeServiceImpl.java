package com.ruoyi.traffic.service.standAdcode.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.traffic.domain.standardAdcode.TrafficStandardAdcode;
import com.ruoyi.traffic.dto.CityDTO;
import com.ruoyi.traffic.dto.DistrictDTO;
import com.ruoyi.traffic.dto.ProvinceDTO;
import com.ruoyi.traffic.dto.VillageDTO;
import com.ruoyi.traffic.mapper.standardAdcode.TrafficStandardAdcodeMapper;
import com.ruoyi.traffic.service.standAdcode.ITrafficStandardAdcodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficStandardAdcodeServiceImpl
 * @author: chengchangli
 * @description: 行政区划编码服务实现类
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Service
public class TrafficStandardAdcodeServiceImpl extends ServiceImpl<TrafficStandardAdcodeMapper, TrafficStandardAdcode>
        implements ITrafficStandardAdcodeService {

    @Override
    public List<ProvinceDTO> getAllProvince() {
        return null;
    }

    @Override
    public List<CityDTO> getAllCityByP(String pCode) {
        return null;
    }

    @Override
    public List<DistrictDTO> getAllDistrictByC(String cCode) {
        return null;
    }

    @Override
    public List<VillageDTO> getAllVillageByD(String dCode) {
        return null;
    }
}
