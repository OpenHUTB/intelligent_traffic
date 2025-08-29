import { createSlice } from '@reduxjs/toolkit'

// 固定时间刻度（组件里使用的）
const timeLabels = [
  '00:00',
  '01:00',
  '02:00',
  '03:00',
  '04:00',
  '05:00',
  '06:00',
  '07:00',
  '08:00',
  '09:00',
  '10:00',
  '11:00',
  '12:00',
]

// 初始固定数据（可按需调整）
const initialState = {
  timeLabels,
  current: [55, 52, 50, 48, 47, 49, 60, 65, 62, 58, 56, 54],
  weekly: [50, 48, 47, 45, 44, 46, 52, 58, 55, 53, 51, 50],
}

const flowSpeedSlice = createSlice({
  name: 'flowSpeed',
  initialState,
  reducers: {
    setFlowSpeedSeries: (state, action) => {
      const { current, weekly } = action.payload
      if (current) state.current = current
      if (weekly) state.weekly = weekly
    },
    applyFlowSpeedUpdate: (state, action) => {
      const { current, weekly } = action.payload || {}
      if (Array.isArray(current)) state.current = current
      if (Array.isArray(weekly)) state.weekly = weekly
    },
  },
})

export const { setFlowSpeedSeries, applyFlowSpeedUpdate } =
  flowSpeedSlice.actions

export default flowSpeedSlice.reducer
