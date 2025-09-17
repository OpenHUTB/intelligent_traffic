package com.ruoyi.simulation.util;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.TrafficLight;
import io.swagger.models.auth.In;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 坐标信息工具类
 */
public class TransferJunctionUtil {
    /**
     * 获取所有路口经纬度对应的坐标集合
     * @param junctionList
     * @return
     */
    private static Map<Integer, Point> transferPoint(GeometryFactory factory, List<Junction> junctionList){
        Map<Integer, Point> pointMap = new HashMap<>();
        for(Junction junction: junctionList){
            Point point = factory.createPoint(new Coordinate(junction.getLongitude(),junction.getLatitude()));
            pointMap.put(junction.getJunctionId(), point);
        }
        return pointMap;
    }

    /**
     * 获取经纬度最近的路口信息
     * @param longitude
     * @param latitude
     * @param junctionList
     * @return
     */
    public static int getJunctionId(Double longitude, Double latitude, List<Junction> junctionList){
        GeometryFactory factory = new GeometryFactory();
        Point current = factory.createPoint(new Coordinate(longitude, latitude));
        Map<Integer, Point> pointMap = transferPoint(factory, junctionList);
        return getNearestJunctionId(current, pointMap);
    }

    /**
     * 获取经纬度最近的路口信息
     * @param current
     * @param pointMap
     * @return
     */
    private static int getNearestJunctionId(Point current, Map<Integer, Point> pointMap){
        int targetId = 0;
        double minDistance = Double.MAX_VALUE;
        for(int junctionId: pointMap.keySet()){
            Point point = pointMap.get(junctionId);
            double distance = current.distance(point);
            if(distance<minDistance){
                minDistance = distance;
                targetId = junctionId;
            }
        }
        return targetId;
    }
    /**
     * 获取每个经纬度对应的路口信息
     * @param locationList
     * @param junctionList
     * @return
     */
    public static List<Integer> getTrafficLightIdList(List<Location> locationList, List<Junction> junctionList, List<TrafficLight> trafficLightList){
        GeometryFactory factory = new GeometryFactory();
        Map<Integer, Point> pointMap = transferPoint(factory, junctionList);
        List<Integer> junctionIdList = new ArrayList<>();
        for(Location location: locationList){
            Point point = factory.createPoint(new Coordinate(location.getLongitude(), location.getLatitude()));
            int junctionId = getNearestJunctionId(point, pointMap);
            junctionIdList.add(junctionId);
        }
        if(junctionIdList.size()<2){
            throw new RuntimeException("绿波线路至少要包含两个路口！");
        }
        List<TrafficLightTrace> traceList = getTraceList(junctionIdList, trafficLightList);
        List<Integer> trafficLightIdList = filterTrafficLight(traceList);
        return trafficLightIdList;
    }
    /**
     * 追踪绿波经过的所有可能的红绿灯集合
     * @param junctionIdList
     * @param trafficLightList
     * @return
     */
    private static List<TrafficLightTrace> getTraceList(List<Integer> junctionIdList, List<TrafficLight> trafficLightList){
        List<TrafficLightTrace> traceList = new ArrayList<>();
        for(int i=1;i<junctionIdList.size();i++){
            int startJunctionId = junctionIdList.get(i-1);
            int endJunctionId = junctionIdList.get(i);
            TrafficLightTrace trace = new TrafficLightTrace();
            List<TrafficLight> startList = getStartTrafficLightId(startJunctionId, endJunctionId, trafficLightList);   //[19,23]       [7,10]
            List<TrafficLight> endList = getEndTrafficLightId(startJunctionId, endJunctionId, trafficLightList);       //[8,7]         [40,2]
            trace.setStartList(startList);
            trace.setEndList(endList);
            traceList.add(trace);
        }
        return traceList;
    }

    /**
     * 对红绿灯集合进行筛选
     * @param traceList
     * @return
     */
    private static List<Integer> filterTrafficLight(List<TrafficLightTrace> traceList){
        List<Integer> trafficLightIdList = new ArrayList<>();
        TrafficLightTrace start = traceList.get(0);
        List<TrafficLight> startList = start.getStartList();
        Integer trafficLightId = forwardFirst(startList);
        if(trafficLightId!=null){
            trafficLightIdList.add(trafficLightId);
        }
        List<TrafficLight> endList = start.getEndList();
        for(int i=1;i<traceList.size();i++){
            start = traceList.get(i);
            startList = start.getStartList();
            trafficLightId = intersetId(startList, endList);
            if(trafficLightId!=null){
                trafficLightIdList.add(trafficLightId);
            }
            endList = start.getEndList();
        }
        trafficLightId = forwardFirst(endList);
        if(trafficLightId!=null){
            trafficLightIdList.add(trafficLightId);
        }
        return trafficLightIdList;
    }
    private static Integer forwardFirst(List<TrafficLight> startList){
        for(TrafficLight trafficLight : startList){
            int trafficLightId = trafficLight.getTrafficLightId();
            if(StringUtils.equals(trafficLight.getTurnDirection(),"FORWARD")){
                return trafficLightId;
            }
        }
        if(startList.size()>0){
            TrafficLight trafficLight = startList.get(0);
            return trafficLight.getTrafficLightId();
        }
        return null;
    }
    private static Integer intersetId(List<TrafficLight> startList, List<TrafficLight> endList){
        List<TrafficLight> trafficLightList = ListUtil.interset(endList, startList);
        return forwardFirst(trafficLightList);
    }
    //-1,6,4
    private static List<TrafficLight> getStartTrafficLightId(int startJunctionId, int endJunctionId, List<TrafficLight> trafficLightList){
        List<TrafficLight> startList = new ArrayList<>();
        for(TrafficLight trafficLight: trafficLightList){
            if(trafficLight.getJunctionId()==null||trafficLight.getNextJunctionId()==null){
                continue;
            }
            if(trafficLight.getJunctionId()==startJunctionId&&trafficLight.getNextJunctionId()==endJunctionId){
                startList.add(trafficLight);;
            }
        }
        return startList;
    }
    private static List<TrafficLight> getEndTrafficLightId(int startJunctionId, int endJunctionId, List<TrafficLight> trafficLightList){
        List<TrafficLight> endList = new ArrayList<>();
        for(TrafficLight trafficLight: trafficLightList){
            if(trafficLight.getPrefixJunctionId()==null||trafficLight.getJunctionId()==null){
                continue;
            }
            if(trafficLight.getPrefixJunctionId()==startJunctionId&&trafficLight.getJunctionId()==endJunctionId){
                endList.add(trafficLight);
            }
        }
        return endList;
    }
}
