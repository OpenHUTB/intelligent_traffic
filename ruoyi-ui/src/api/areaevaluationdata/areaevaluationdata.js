import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

//新增区域实时评价的接口
export function addAreaevaluationdata(data) {
    return request({
        url: '/traffic/areaevaluationdata/add',
        method: 'post',
        data: data
    })
}
//删除区域实时评价的接口
export function deleteAreaevaluationdata(data) {
    return request({
        url: '/traffic/areaevaluationdata/delete',
        method: 'post',
        data: data
    })
}
//获取区域实时评价详情的接口
export function findByIdAreaevaluationdata(areaevaluationdataId) {
    return request({
        url: '/traffic/areaevaluationdata/findById/' + parseStrEmpty(areaevaluationdataId),
        method: 'get'
    })
}
//分页获取区域实时评价的接口
export function pageAreaevaluationdata(data) {
    return request({
        url: '/traffic/areaevaluationdata/page',
        method: 'post',
        data: data
    })
}
//获取区域实时评价指标排名的接口
export function queryRankAreaevaluationdata(data) {
    return request({
        url: '/traffic/areaevaluationdata/queryRank',
        method: 'post',
        data: data
    })
}