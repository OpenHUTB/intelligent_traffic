import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 分页获取不同纬度下全部路口的接口
export function queryRankIntersection(data) {
    return request({
        url: '/traffic/intersection/queryRank',
        method: 'post',
        data: data
    })
}
// 删除路口的接口
export function deleteIntersection(data) {
    return request({
        url: '/traffic/intersection/delete',
        method: 'post',
        data: data
    })
}
// 添加路口的接口
export function addIntersection(data) {
    return request({
        url: '/traffic/intersection/add',
        method: 'post',
        data: data
    })
}
// 编辑路口信息的接口
export function updateIntersection(data) {
    return request({
        url: '/traffic/intersection/update',
        method: 'post',
        data: data
    })
}
// 查询路口详情的接口
export function findByIdIntersection(intersectionId) {
    return request({
        url: '/traffic/intersection/findById/' + parseStrEmpty(intersectionId),
        method: 'get'
    })
}
