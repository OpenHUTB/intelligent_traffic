import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 指标列表
export function listEvaluationType(data) {
  return request({
    url: '/traffic/evaluationType/page',
    method: 'post',
    data: data
  })
}

// 查询指标详细
export function getEvaluationType(evaluationTypeId) {
  return request({
    url: '/traffic/evaluationType/findById/' + parseStrEmpty(evaluationTypeId),
    method: 'get'
  })
}

// 新增指标类型
export function addEvaluationType(data) {
  return request({
    url: '/traffic/evaluationType/add',
    method: 'post',
    data: data
  })
}

// 修改指标类型
export function updateEvaluationType(data) {
  return request({
    url: '/traffic/evaluationType/update',
    method: 'post',
    data: data
  })
}

// 删除指标类型
export function delEvaluationType(data) {
  return request({
    url: '/traffic/evaluationType/delete',
    method: 'post',
    data: data
  })
}




