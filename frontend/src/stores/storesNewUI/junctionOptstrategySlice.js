import { createSlice } from '@reduxjs/toolkit'

// 三段可配置文字：原方案 / 当前方案 / 协调效果跟踪
// 可在任意地方 dispatch 对应 action 来实时更新
const initialState = {
  previousStr: '工作日场景，晴天模式，手动调控，直行与左转同级控制',
  currentOptr: '周末晚高峰场景，夜晚雨天模式，自动调优，直行优先',
  result: '通过区间时间与停车次数统计',
}

const junctionOptstrategySlice = createSlice({
  name: 'junctionOptstrategy',
  initialState,
  reducers: {
    setPreviousStr: (state, action) => {
      state.previousStr = action.payload
    },
    setCurrentOptr: (state, action) => {
      state.currentOptr = action.payload
    },
    setResult: (state, action) => {
      state.result = action.payload
    },
    // 一次性更新全部（可部分提供）
    setAllOptstrategyText: (state, action) => {
      const p = action.payload || {}
      if (p.previousStr !== undefined) state.previousStr = p.previousStr
      if (p.currentOptr !== undefined) state.currentOptr = p.currentOptr
      if (p.result !== undefined) state.result = p.result
    },
    resetOptstrategyText: () => initialState,
  },
})

export const {
  setPreviousStr,
  setCurrentOptr,
  setResult,
  setAllOptstrategyText,
  resetOptstrategyText,
} = junctionOptstrategySlice.actions

export default junctionOptstrategySlice.reducer
