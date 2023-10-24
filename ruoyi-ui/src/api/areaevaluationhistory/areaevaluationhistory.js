import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


// 删除区域历史评价的接口
export function deleteAreaevaluationhistory(data) {
    return request({
        url: '/traffic/areaevaluationhistory/delete',
        method: 'post',
        data: data
    })
}
// 获取区域历史评价详情的接口
export function findByIdAreaevaluationhistory(areaevaluationhistoryId) {
    return request({
        url: '/traffic/areaevaluationhistory/findById/' + parseStrEmpty(areaevaluationhistoryId),
        method: 'get'
    })
}
//分页获取区域历史评价的接口
export function pageAreaevaluationhistory(data) {
    return request({
        url: '/traffic/areaevaluationhistory/page',
        method: 'post',
        data: data
    })
}
// 获取区域历史评价指标排名的接口
export function queryRankAreaevaluationhistory(data) {
    return request({
        url: 'traffic/areaevaluationhistory/queryRank',
        method: 'post',
        data: data
    })
}
// 编辑区域历史评价指标排名的接口
export function updateAreaevaluationhistory(data) {
    return request({
        url: '/traffic/areaevaluationhistory/update',
        method: 'post',
        data: data
    })
}