import { createSlice } from '@reduxjs/toolkit'

// 初始静态数据（与组件原始静态数组对应）
const initialState = {
  listItems: [
    {
      name: '岳麓西大道',
      index: '2',
      distance: '5',
      status: '否',
      speed: '33',
      trend: '-3%',
    },
    {
      name: '旺龙路',
      index: '1',
      distance: '2',
      status: '异常',
      speed: '5',
      trend: '-3%',
    },
    {
      name: '尖山路',
      index: '5',
      distance: '5',
      status: '否',
      speed: '15',
      trend: '-3%',
    },
    {
      name: '望青路',
      index: '6',
      distance: '5',
      status: '否',
      speed: '20',
      trend: '-3%',
    },
    {
      name: '青山路',
      index: '7',
      distance: '5',
      status: '否',
      speed: '62',
      trend: '-3%',
    },
  ],
}

const trafficCongestionSlice = createSlice({
  name: 'trafficCongestion',
  initialState,
  reducers: {
    setTrafficCongestionList: (state, action) => {
      state.listItems = action.payload
    },
    updateTrafficCongestionItem: (state, action) => {
      const { index, data } = action.payload || {}
      if (index != null && index >= 0 && index < state.listItems.length) {
        state.listItems[index] = { ...state.listItems[index], ...data }
      }
    },
    addTrafficCongestionItem: (state, action) => {
      state.listItems.unshift(action.payload)
    },
    clearTrafficCongestion: (state) => {
      state.listItems = []
    },
  },
})

export const {
  setTrafficCongestionList,
  updateTrafficCongestionItem,
  addTrafficCongestionItem,
  clearTrafficCongestion,
} = trafficCongestionSlice.actions

export default trafficCongestionSlice.reducer
