// signalJunctionSlice.js

import { createSlice } from '@reduxjs/toolkit'
import { time } from 'echarts'

const initialState = {
  listItems: [
    {
      name: '尖山路与岳麓西大道交叉口',
      congestion: 18.53,
      time: 18.53,
      trend: 8.2,
      trendDirection: true,
    },
    {
      name: '青山路与尖山路交叉口',
      congestion: 20.53,
      time: 20.53,
      trend: 11.2,
      trendDirection: true,
    },
    {
      name: '旺龙路与青山路交叉口',
      congestion: 15.53,
      time: 15.53,
      trend: 5.2,
      trendDirection: false,
    },
    {
      name: '旺龙路与青山路交叉口',
      congestion: 15.53,
      time: 15.53,
      trend: 5.2,
      trendDirection: false,
    },
    {
      name: '旺龙路与青山路交叉口',
      congestion: 15.53,
      time: 15.53,
      trend: 5.2,
      trendDirection: false,
    },
  ],
}

const signalJunctionSlice = createSlice({
  name: 'signalJunction',
  initialState,
  reducers: {
    setListItems: (state, action) => {
      state.listItems = action.payload
    },
  },
})

export const { setListItems } = signalJunctionSlice.actions

export default signalJunctionSlice.reducer
