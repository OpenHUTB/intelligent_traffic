import { createSlice } from '@reduxjs/toolkit'

// 极简：只三段可变文字（原方案 / 当前方案 / 优化效果）
// 若需要换行，在字符串里使用 \n
const initialState = {
  previousPlanText: '路线1，路线2，路线3，绿波持续时间15分钟',
  currentPlanText:
    '路口1： 红灯40秒，绿灯12秒，黄灯3秒\n路口2： 红灯35秒，绿灯15秒，黄灯3秒',
  optimizeResultText: '通过区间时间与停车次数统计',
}

const junctionOptSlice = createSlice({
  name: 'junctionOpt',
  initialState,
  reducers: {
    setPreviousPlanText: (state, action) => {
      state.previousPlanText = action.payload
    },
    setCurrentPlanText: (state, action) => {
      state.currentPlanText = action.payload
    },
    setOptimizeResultText: (state, action) => {
      state.optimizeResultText = action.payload
    },
    // 一次性整体替换三段文字
    setAllTexts: (state, action) => {
      const p = action.payload || {}
      if (p.previousPlanText !== undefined)
        state.previousPlanText = p.previousPlanText
      if (p.currentPlanText !== undefined)
        state.currentPlanText = p.currentPlanText
      if (p.optimizeResultText !== undefined)
        state.optimizeResultText = p.optimizeResultText
    },
  },
})

export const {
  setPreviousPlanText,
  setCurrentPlanText,
  setOptimizeResultText,
  setAllTexts,
} = junctionOptSlice.actions

export default junctionOptSlice.reducer
