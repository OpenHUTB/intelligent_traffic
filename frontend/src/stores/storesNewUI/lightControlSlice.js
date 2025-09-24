// 仅保留组件需要的绿波路段数据（扁平结构，包含路名及信号配时）
import { createSlice } from '@reduxjs/toolkit'

// 如果有更多路段，可继续追加；yellowDurationTime 可单独配置，默认 3 秒
const initialState = [
  {
    name: '尖山路',
    redDurationTime: 146,
    greenDurationTime: 9,
    yellowDurationTime: 3,
  },
  {
    name: '旺龙路',
    redDurationTime: 84,
    greenDurationTime: 71,
    yellowDurationTime: 3,
  },
  {
    name: '望青路',
    redDurationTime: 110,
    greenDurationTime: 45,
    yellowDurationTime: 3,
  },
  {
    name: '岳麓西大道',
    redDurationTime: 110,
    greenDurationTime: 35,
    yellowDurationTime: 3,
  },
  {
    name: '天顶路',
    redDurationTime: 110,
    greenDurationTime: 40,
    yellowDurationTime: 3,
  },
]

const greenFlowSlice = createSlice({
  name: 'greenFlow',
  initialState,
  reducers: {
    // 直接整体替换：payload 应该是上述数组结构
    setLight: (state, action) => action.payload,
    // 可选：更新单个路段（通过 name 匹配）
    updateRoad: (state, action) => {
      const { name, ...rest } = action.payload || {}
      const idx = state.findIndex((r) => r.name === name)
      if (idx !== -1) {
        state[idx] = { ...state[idx], ...rest }
      }
    },
  },
})

export const { setLight, updateRoad } = greenFlowSlice.actions
export default greenFlowSlice.reducer
