package com.ruoyi.simulation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.dao.GreenWaveMapper;
import com.ruoyi.simulation.dao.JunctionMapper;
import com.ruoyi.simulation.dao.TrafficLightMapper;
import com.ruoyi.simulation.domain.GreenWave;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.TrafficLight;
import com.ruoyi.simulation.util.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 绿波信息业务层实现类
 */
@Service
public class GreenWaveServiceImpl implements GreenWaveService {
    @Autowired
    private TrafficLightMapper trafficLightMapper;
    @Autowired
    private GreenWaveMapper greenWaveMapper;
    @Autowired
    private JunctionMapper junctionMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private Environment environment;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultUtil<Void> addGreenWave(List<Location> locationList) {
        ResultUtil<Void> result = new ResultUtil<>();
        //获取所在地图
        String area = this.environment.getProperty("simulation.world.map");
        //获取所有路口信息
        LambdaQueryWrapper<Junction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Junction::getArea, area);
        List<Junction> junctionList = this.junctionMapper.selectList(queryWrapper);
        //获取所有红绿灯信息列表
        List<TrafficLight> trafficLightList = this.trafficLightMapper.selectList(new LambdaQueryWrapper<>());
        Map<Integer, Integer> junctionIdMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            junctionIdMap.put(trafficLight.getTrafficLightId(), trafficLight.getJunctionId());
        }
        //获取所有坐标对应的红绿灯ID集合
        List<Integer> trafficLightIdList = TransferJunctionUtil.getTrafficLightIdList(locationList, junctionList, trafficLightList);
        //1.绿波所在路口(junctionId)不存在环路
        checkCycle(trafficLightIdList, junctionIdMap);
        //删除之前的绿波线路
        this.greenWaveMapper.delete(new LambdaQueryWrapper<>());
        //插入绿波线路
        List<GreenWave> waveList = this.insertWaveList(trafficLightIdList, junctionIdMap, 1);
        FixedRegulation.setGreenWave(waveList, this.redisTemplate);
        result.setStatus(ResultUtil.Status.SUCCESS);
        return result;
    }
    /**
     * 检测绿波线路是否本身存在环路
     * @param trafficLightIdList
     * @return
     */
    private List<Integer> checkCycle(List<Integer> trafficLightIdList, Map<Integer, Integer> junctionIdMap){
        List<Integer> junctionIdList = new ArrayList<>();
        for(int trafficLightId: trafficLightIdList){
            int junctionId = junctionIdMap.get(trafficLightId);
            junctionIdList.add(junctionId);
        }
        if(new HashSet<Integer>(junctionIdList).size()!=junctionIdList.size()){
            throw new RuntimeException("绿波线路设置不能存在环路！");
        }
        return junctionIdList;
    }

    /**
     * 检测绿波线路是否和已存在的绿波线路存在两个以上重复路口
     * @param trafficLightList
     * @param junctionIdList
     * @return
     */
    private int checkRepeat(List<TrafficLight> trafficLightList, List<Integer> junctionIdList){
        Map<Integer,Integer> trafficLightJunctionMap = trafficLightList.stream()
                .collect(Collectors.toMap(TrafficLight::getTrafficLightId, TrafficLight::getJunctionId));
        List<GreenWave> greenWaveList = this.greenWaveMapper.selectList(new LambdaQueryWrapper<>());
        Map<Integer,List<Integer>> groupJunctionIdMap = new HashMap<>();
        int maxGroupId = 0;
        for(GreenWave greenWave: greenWaveList){
            int groupId = greenWave.getGroupId();
            int trafficLightId= greenWave.getTrafficLightId();
            int junctionId = trafficLightJunctionMap.get(trafficLightId);
            groupJunctionIdMap.computeIfAbsent(groupId, key->new ArrayList<>()).add(junctionId);
            if(groupId>maxGroupId){
                maxGroupId = groupId;
            }
        }
        for(int groupId: groupJunctionIdMap.keySet()){
            List<Integer> intersetList = ListUtil.interset(junctionIdList, groupJunctionIdMap.get(groupId));
            if(intersetList.size()>=2){
                throw new RuntimeException("待添加的绿波线路和已存在的绿波线路不能存在两个以上相同的路口！");
            }
        }
        return maxGroupId;
    }
    /**
     * 批量插入绿波信息
     * @param trafficLightIdList
     * @param groupId
     */
    private List<GreenWave> insertWaveList(List<Integer> trafficLightIdList, Map<Integer, Integer> junctionIdMap, int groupId){
        List<GreenWave> greenWaveList = new ArrayList<>();
        for(int i=0;i<trafficLightIdList.size()-1;i++){
            int trafficLightId = trafficLightIdList.get(i);
            int nextId = trafficLightIdList.get(i+1);
            int junctionId = junctionIdMap.get(trafficLightId);
            GreenWave greenWave = new GreenWave();
            greenWave.setTrafficLightId(trafficLightId);
            greenWave.setNextId(nextId);
            greenWave.setGroupId(groupId);
            greenWave.setJunctionId(junctionId);
            greenWaveList.add(greenWave);
        }
        int trafficLightId = trafficLightIdList.get(trafficLightIdList.size()-1);
        int junctionId = junctionIdMap.get(trafficLightId);
        GreenWave greenWave = new GreenWave();
        greenWave.setTrafficLightId(trafficLightId);
        greenWave.setGroupId(groupId);
        greenWave.setJunctionId(junctionId);
        greenWaveList.add(greenWave);
        this.greenWaveMapper.insertList(greenWaveList);
        return greenWaveList;
    }
}
