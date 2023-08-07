package com.ruoyi.traffic.service.standAdcode;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.standardAdcode.TrafficStandardAdcode;
import com.ruoyi.traffic.dto.CityDTO;
import com.ruoyi.traffic.dto.DistrictDTO;
import com.ruoyi.traffic.dto.ProvinceDTO;
import com.ruoyi.traffic.dto.VillageDTO;

import java.util.List;

/**
 * @classname: ITrafficStandardAdcodeService
 * @author: chengchangli
 * @description: 行政区划编码服务类
 * @date: 2023/8/3
 * @version: v1.0
 **/
public interface ITrafficStandardAdcodeService extends IService<TrafficStandardAdcode> {

    // 获取所有省份
    List<ProvinceDTO> getAllProvince();

    // 根据省份获取其所有城市
    List<CityDTO> getAllCityByP (String pCode);

    // 根据城市获取其所有区县
    List<DistrictDTO> getAllDistrictByC (String cCode);

    // 根据区县获取其所有乡镇
    List<VillageDTO> getAllVillageByD(String dCode);
}
