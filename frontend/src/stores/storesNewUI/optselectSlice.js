import { createSlice } from '@reduxjs/toolkit'

// 固定时间刻度（组件里使用的）
const timeLabels = [
  '旺龙路与尖山路',
  '旺龙路与青山路',
  '青山路与尖山路',
  '望青路与青山路',
  '岳麓大道与旺龙路',
  '岳麓大道与麓谷大道',
]

// 初始固定数据（可按需调整）
const initialState = {
  timeLabels,
  current: [32, 44, 25, 28, 29, 86],
  weekly: [39, 57, 30, 33, 33, 115],
}

const optSelectSlice = createSlice({
  name: 'optSelect',
  initialState,
  reducers: {
    setOptSelectSeries: (state, action) => {
      const { current, weekly } = action.payload
      if (current) state.current = current
      if (weekly) state.weekly = weekly
    },
    applyOptSelectUpdate: (state, action) => {
      const { current, weekly } = action.payload || {}
      if (Array.isArray(current)) state.current = current
      if (Array.isArray(weekly)) state.weekly = weekly
    },
  },
})

export const { setOptSelectSeries, applyOptSelectUpdate } =
  optSelectSlice.actions

export default optSelectSlice.reducer
