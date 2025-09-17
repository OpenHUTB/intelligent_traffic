package com.ruoyi.simulation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.SectionMapper;
import com.ruoyi.simulation.dao.JunctionMapper;
import com.ruoyi.simulation.domain.Section;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.util.CruiseData;
import com.ruoyi.simulation.util.Location;
import com.ruoyi.simulation.util.ResultUtil;
import com.ruoyi.simulation.util.TransferJunctionUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 路段信息业务层实现类
 */
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private JunctionMapper junctionMapper;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private Environment environment;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public ResultUtil<Void> cruise(CruiseData cruiseData) {
        ResultUtil<Void> result = new ResultUtil<>();
        List<Integer>junctionIdList = cruiseData.getJunctionIdList();
        if(junctionIdList.size()>1){
            List<String> pointList = new ArrayList<>();
            List<Section> coordinateList = this.sectionMapper.selectList(new LambdaQueryWrapper<>());
            for(int i=0;i<junctionIdList.size()-1;i++){
                int startJunctionId = junctionIdList.get(i);
                int endJunctionId = junctionIdList.get(i+1);
                pointList.addAll(this.getVirtualPoints(startJunctionId, endJunctionId, coordinateList));
            }
            cruiseData.setPitch(-10.0);
            cruiseData.setRoute("[" + StringUtils.join(pointList) + "]");
            this.redisTemplate.opsForValue().set("cruise_data", cruiseData);
            this.callUE4Engine.executeExamples("cruise_function.py");
            result.setStatus(ResultUtil.Status.SUCCESS);
        }
        return result;
    }

    @Override
    public ResultUtil<Void> transferJunction(Location location) {
        ResultUtil<Void> result = new ResultUtil<>();
        //获取所在地图
        String area = this.environment.getProperty("simulation.world.map");
        LambdaQueryWrapper<Junction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Junction::getArea, area);
        //获取所有路口信息
        List<Junction> junctionList = this.junctionMapper.selectList(queryWrapper);
        int junctionId = TransferJunctionUtil.getJunctionId(location.getLongitude(), location.getLatitude(), junctionList);
        //执行进入某个路口的操作
        this.callUE4Engine.executeExamples("enter_intersection.py --id "+(junctionId-1));
        result.setStatus(ResultUtil.Status.SUCCESS);
        return result;
    }

    /**
     * 获取两个路口端点对应的航点集合
     * @param startJunctionId
     * @param endJunctionId
     * @param coordinateList
     */
    private List<String> getVirtualPoints(int startJunctionId, int endJunctionId, List<Section> coordinateList){
        List<String> pointList = new ArrayList<>();
        for(Section coordinate: coordinateList){
            if(coordinate.getStartJunctionId()==startJunctionId&&coordinate.getEndJunctionId()==endJunctionId){
                pointList = handlePoints(coordinate.getVirtualPoints());
                break;
            }else if(coordinate.getEndJunctionId()==startJunctionId&&coordinate.getStartJunctionId()==endJunctionId){
                pointList = handlePoints(coordinate.getVirtualPoints());
                Collections.reverse(pointList);
                break;
            }
        }
        return pointList;
    }

    /**
     * 提取元祖数据
     * @param virtualPoints
     * @return
     */
    private static List<String> handlePoints(String virtualPoints){
        System.out.println(virtualPoints);
        System.out.println("--------------------------------------------");
        String[] points= virtualPoints.replace("[(","").replace(")]","").split("\\)\\, \\(");
        List<String> pointList = Arrays.asList(points);
        for(int i=0;i<pointList.size();i++){
            pointList.set(i, "("+pointList.get(i)+")");
        }
        return pointList;
    }
}


