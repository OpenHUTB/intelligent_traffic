import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 分页获取不同纬度下全部路口排名的接口
export function pageIntersectionData(data) {
    return request({
        url: '/traffic/intersectionData/page',
        method: 'post',
        data: data
    })
}
// 添加路口数据的接口
export function addIntersectionData(data) {
    return request({
        url: '/traffic/intersectionData/add',
        method: 'post',
        data: data
    })
}
// 删除路口数据的接口
export function deleteIntersectionData(data) {
    return request({
        url: '/traffic/intersectionData/delete',
        method: 'post',
        data: data
    })
}
// 查询路口数据详情的接口
export function findByIdIntersectionData(intersectionDataId) {
    return request({
        url: '/traffic/intersectionData/findById/' + parseStrEmpty(intersectionDataId),
        method: 'get'
    })
}
