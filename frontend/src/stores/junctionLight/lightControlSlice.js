// src/store/trafficLightsSlice.js
import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  north: {
    left: {
      redDurationTime: 146,
      greenDurationTime: 9,
      initialLight: 'red',
    },
    forward: {
      redDurationTime: 146,
      greenDurationTime: 9,
      initialLight: 'green',
    },
  },
  east: {
    left: {
      redDurationTime: 84,
      greenDurationTime: 71,
      initialLight: 'red',
    },
    forward: {
      redDurationTime: 84,
      greenDurationTime: 43,
      initialLight: 'green',
    },
  },
  south: {
    left: {
      redDurationTime: 110,
      greenDurationTime: 45,
      initialLight: 'red',
    },
    forward: {
      redDurationTime: 110,
      greenDurationTime: 45,
      initialLight: 'green',
    },
  },
  west: {
    left: {
      redDurationTime: 110,
      greenDurationTime: 35,
      initialLight: 'red',
    },
    forward: {
      redDurationTime: 110,
      greenDurationTime: 40,
      initialLight: 'green',
    },
  },
}

const lightControlSlice = createSlice({
  name: 'lightControl',
  initialState,
  reducers: {
    setLight: (state, action) => {
      // 假设 action.payload 是与 initialState 结构一致的对象
      return action.payload
    },
  },
})

export const { setLight } = lightControlSlice.actions

export default lightControlSlice.reducer
