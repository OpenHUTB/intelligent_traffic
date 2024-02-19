import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 根据车牌号查询公交车GPS数据
export function getBusGpsData(busNumber) {
  return request({
    url: '/traffic/busData/findByBusNumber/' + parseStrEmpty(busNumber),
    method: 'get'
  })
}
