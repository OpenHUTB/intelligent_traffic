import { createSlice } from '@reduxjs/toolkit'

// 三段可配置文字：原方案 / 当前方案 / 协调效果跟踪
// 可在任意地方 dispatch 对应 action 来实时更新
const initialState = {
  previousStr: '工作日场景，晴天模式，手动调控，执行与左转同级控制',
  currentOptr:
    '工作日策略，交通流呈现早晚高峰明显、午间相对平稳的特征。为保障通勤高峰时段道路畅通，系统采用了高峰优先配时策略，重点对岳麓西大道与望青路交叉口、岳麓西大道与旺龙路交叉口的信号灯进行了绿灯时长延长及相位优化，提升主干道通行效率并减少排队长度。',
  result:
    '各路口的车辆排队长度平均下降27.7%，车辆延误时间下降37%，平均每分钟吞吐量上升25.8%',
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
