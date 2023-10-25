import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 分页获取不同纬度下全部路口历史数据的接口
export function pageIntersectionHistory(data) {
    return request({
        url: '/traffic/intersectionHistory/page',
        method: 'post',
        data: data
    })
}
// 删除路口历史数据的接口
export function deleteIntersectionHistory(data) {
    return request({
        url: '/traffic/intersectionHistory/delete',
        method: 'post',
        data: data
    })
}
// 查询路口历史数据详情的接口
export function findByIdIntersectionHistory(intersectionHistoryId) {
    return request({
        url: '/traffic/intersectionHistory/findById/' + parseStrEmpty(intersectionHistoryId),
        method: 'get'
    })
}

