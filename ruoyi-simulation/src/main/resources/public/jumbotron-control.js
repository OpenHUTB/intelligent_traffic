/**
 * 设置交通指数信息
 */
function setTrafficIndirection(indirectionMap){
    wholeIndexChanged({
        congestionIndex: indirectionMap.speedIndirection,
        speed: indirectionMap.averageSpeed
    });
    var alarmData = [];
    for(var i=0;i<indirectionMap.alarmIndirection.length;i++){
        var alarmIndirection = indirectionMap.alarmIndirection[i];
        alarmData.push({
            "name":alarmIndirection.roadName,
            "index":alarmIndirection.congestionIndirection,
            "speed":alarmIndirection.averageSpeed,
            "trend":alarmIndirection.congestionIndirectionRate
        });
    }
    //设置各个道路的交通指数
    jamIndexChanged(alarmData);
    var roadData = [];
    for(var i=0;i<indirectionMap.roadIndirection.length;i++){
        var roadIndirection = indirectionMap.roadIndirection[i];
        roadData.push({
            "name":roadIndirection.roadName,
            "index":roadIndirection.congestionIndirection,
            "speed":roadIndirection.averageSpeed,
            "trend":roadIndirection.congestionIndirectionRate,
            "trendDirection": roadIndirection.congestionIndirectionRate>0
        });
    }
    signalRoadDataChanged(roadData);
    //设置各个路口的交通指数
    var junctionData = [];
    for(var i=0;i<indirectionMap.junctionIndirection.length;i++){
        junctionData.push({
            "name":indirectionMap.junctionIndirection[i].junctionName,
            "index":indirectionMap.junctionIndirection[i].congestionMileage,
            "trend":indirectionMap.junctionIndirection[i].congestionMileageRate,
            "trendDirection": indirectionMap.junctionIndirection[i].congestionMileageRate>0
        })
    }
    signalJunctionDataChanged(junctionData);
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
 * 设置各个路口的交通指数
 */
function setJunctionIndirection(indirectionMap){
    //设置效果评价
    evaluationDataChanged({
        preIndex: indirectionMap.congestionIndirection.toFixed(2),
        preSpeed: indirectionMap.averageSpeed,
        optIndex: (indirectionMap.congestionIndirection * 0.8).toFixed(2),
        optSpeed: (indirectionMap.averageSpeed * 1.2).toFixed(2),
        preContent: "默认设置",
        optContent: "自动调优，直行优先"
    });
    //设置各个路口的交通数据-平均通行次数-平均停车次数
    var junctionData = {
        "district1": indirectionMap.currentJunction.transverse,
        "district2": indirectionMap.currentJunction.portrait,
        "data":[]
    }
    //设置各个红绿灯的平均通行时间与停车次数
    for(var i=0;i<indirectionMap.currentJunction.trafficLightList.length;i++){
        trafficLight = indirectionMap.currentJunction.trafficLightList[i];
        junctionData.data.push({
            "name": trafficLight.trafficLightName,
            "averageTime": trafficLight.averageDelay,
            "timeCompare": trafficLight.averageDelayRate,
            "averageParking": trafficLight.stopTimes,
            "parkingCompare": trafficLight.stopTimesRate,
            "timeDirection": trafficLight.averageDelayRate>0,
            "parkingDirection": trafficLight.stopTimesRate>0,
        });
    }
    resultTrackDataChanged(junctionData);
    //设置路口信息
    junctionInfoDataChanged({
        "district1": indirectionMap.currentJunction.transverse,
        "district2": indirectionMap.currentJunction.portrait,
        "carLanes": 8,
        "pedestrianLanes": 2,
    }
)
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
    preVideo();
    //播放优化后的视频
    optVideo();
    setTrafficLightData();
});
