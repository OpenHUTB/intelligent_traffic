import { createSlice } from '@reduxjs/toolkit'

// 路口信控优化效果分析折线图数据 Slice
// 时间轴固定在组件中，这里只管理两组数据数组
// currentFlow: 当前流量 (单位：分钟)  weekAvgFlow: 周均流量 (单位：分钟)
const initialState = {
  currentFlow: [50, 48, 45, 43, 40, 38, 55, 60, 58, 57, 52, 50, 49],
  weekAvgFlow: [46, 44, 42, 40, 39, 37, 50, 54, 53, 52, 48, 47, 46],
}

const junctionOptResultSlice = createSlice({
  name: 'junctionOptResult',
  initialState,
  reducers: {
    setCurrentFlow: (state, action) => {
      state.currentFlow = action.payload
    },
    setWeekAvgFlow: (state, action) => {
      state.weekAvgFlow = action.payload
    },
    setBothFlows: (state, action) => {
      const { currentFlow, weekAvgFlow } = action.payload || {}
      if (currentFlow !== undefined) state.currentFlow = currentFlow
      if (weekAvgFlow !== undefined) state.weekAvgFlow = weekAvgFlow
    },
    resetFlows: () => initialState,
  },
})

export const { setCurrentFlow, setWeekAvgFlow, setBothFlows, resetFlows } =
  junctionOptResultSlice.actions

export default junctionOptResultSlice.reducer
