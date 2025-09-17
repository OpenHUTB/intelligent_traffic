var alarmingLevelMap = {
    "MINOR":"低",
    "GENERAL":"普通",
    "SERIOUS":"高",
    "FATAL":"极高"
}
var alarmingTypeMap = {
    "WRONG_WAY":"逆行",
    "REVERSE":"异常倒车",
    "ABNORMAL_STOP":"异常停车",
    "LOWER_SPEED":"超低速"
}
/**
 * 设置交通指数信息
 */
function setTrafficIndirection(indirectionMap){
    setGlobalIndirection(indirectionMap);
    setRoadIndirection(indirectionMap);
    setJunctionIndirection(indirectionMap);
    setAlarming(indirectionMap);
    //设置主页右侧部分指数
    optimiseOverviewChanged({
        "averageDelay": indirectionMap.averageDelayChange,
        "delayDirection": indirectionMap.averageDelayChange>0,
        "congestionMileage": indirectionMap.congestionMileageRate,
        "mileageDirection": indirectionMap.congestionMileageRate>0,
        "averageSpeed": indirectionMap.averageSpeedRate,
        "speedDirection": indirectionMap.averageSpeedRate>0,
    });
    optimiseListChanged([
        {"name":"优化路口占比(%)", "value": 0 },
        {"name":"优化次数(次)", "value": 0 },
        {"name":"服务车次（万辆）", "value": indirectionMap.servicedVehicle},
        {"name":"路网车辆 (万辆)", "value": indirectionMap.totalVehicle},
    ]);
}
/**
 * 设置全局指数
 */
function setGlobalIndirection(indirectionMap){
    //设置全局交通指数和平均速度
    updateDistrictSpeed({
        congestionIndex: indirectionMap.speedIndirection,
        speed: indirectionMap.averageSpeed
    });
    //设置交通拥堵指数
    updateTrafficInfo('index',indirectionMap.congestionIndirection);
    //修改拥堵里程
    updateTrafficInfo('congestion', indirectionMap.congestionMileage);
    //修改拥堵路段总数
    updateTrafficInfo('road',3);
    //修改拥堵时长
    updateTrafficInfo('time',indirectionMap.averageDelay);
}
/**
 * 设置各道路指数
 */
function setRoadIndirection(indirectionMap){
    var roadIndirectionList = [];
    for(var i=0;i<indirectionMap.roadIndirection.length;i++){
        var roadIndirection = indirectionMap.roadIndirection[i];
        roadIndirectionList.push({
            "name":roadIndirection.roadName,
            "index":roadIndirection.congestionIndirection,
            "speed":roadIndirection.averageSpeed,
            "trend":roadIndirection.congestionIndirectionRate+"%",
            "trendDirection": roadIndirection.congestionIndirectionRate>0,
            'status':'无'
        });
    }
    signalRoadDataChanged(roadIndirectionList);
}
/**
 * 设置各个路口的交通指数
 */
function setJunctionIndirection(indirectionMap){
    //设置各个路口的交通指数
    var junctionData = [];
    for(var i=0;i<indirectionMap.junctionIndirection.length;i++){
        var junctionIndirection = indirectionMap.junctionIndirection[i];
        junctionData.push({
            "name":junctionIndirection.junctionName,
            "congestion":junctionIndirection.congestionMileage,
            "time": junctionIndirection.averageDelay,
            "trend":junctionIndirection.averageDelayRate,
            "trendDirection": junctionIndirection.averageDelayRate>0
        });
    }
    signalJunctionDataChanged(junctionData);
}
/**
 * 设置告警信息
 */
function setAlarming(indirectionMap){
    const alarmData = [];
    if(indirectionMap.alarmingList.length>5){
        while(alarmData.length<5&&indirectionMap.alarmingList.length>0){
            // 生成随机索引
            const index = Math.floor(Math.random() * indirectionMap.alarmingList.length);
            // 记录被删除的元素（可选）
            const alarm = indirectionMap.alarmingList[index];
            var alarmingType = alarmingTypeMap[alarm.type];
            var alarmingLevel = alarmingLevelMap[alarm.level];
            alarmData.push({
                "name":alarmingType,
                "position": alarm.location.longitude.toFixed(10)+","+alarm.location.latitude.toFixed(10),
                "number": alarm.plate.substring(0,7),
                "status": alarmingLevel,
                "isAlert": "是",
                "isDeal": "否",
                "speed": alarm.speed,
                "time": new Date(alarm.time).toLocaleString()
            });
            // 删除该索引位置的元素
            indirectionMap.alarmingList.splice(index, 1);
        }
    }else{
        for(let i=0;i<indirectionMap.alarmingList.length;i++){
            const alarm = indirectionMap.alarmingList[i];
            var alarmingType = alarmingTypeMap[alarm.type];
            var alarmingLevel = alarmingLevelMap[alarm.level];
            alarmData.push({
                "name":alarmingType,
                "position": alarm.location.longitude.toFixed(10)+","+alarm.location.latitude.toFixed(10),
                "number": alarm.plate.substring(0,7),
                "status": alarmingLevel,
                "isAlert": "是",
                "isDeal": "否",
                "speed": alarm.speed,
                "time": new Date(alarm.time).toLocaleString()
            });
        }
    }
    scrollAlertDataChanged(alarmData);
}
/**
 * 初始化红绿灯数据
 */
function setTrafficLightData(){
    var trafficData = sessionStorage['trafficData'];
    if(trafficData!=null){
        trafficData = JSON.parse(trafficData);
        setTimeout(function(){
            lightControlDataChanged(trafficData);
        },"500");
    }
}
$(function(){
    //播放优化前的视频
    //preVideo();
    //播放优化后的视频
    //optVideo();
    setTrafficLightData();
});
