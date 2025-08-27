import { createSlice } from '@reduxjs/toolkit'

// 控制 JunctionTimeprogress 组件中原先硬编码的三个数据对象
const initialState = {
  directionMap: {
    north: '北向',
    east: '东向',
    south: '南向',
    west: '西向',
  },
  movementMap: {
    left: '左转',
    forward: '直行',
  },
  trafficState: {
    north: {
      left: {
        redDurationTime: 140,
        greenDurationTime: 20,
        initialLight: 'red',
      },
      forward: {
        redDurationTime: 140,
        greenDurationTime: 20,
        initialLight: 'green',
      },
    },
    east: {
      left: {
        redDurationTime: 120,
        greenDurationTime: 40,
        initialLight: 'red',
      },
      forward: {
        redDurationTime: 130,
        greenDurationTime: 30,
        initialLight: 'green',
      },
    },
    south: {
      left: { redDurationTime: 90, greenDurationTime: 70, initialLight: 'red' },
      forward: {
        redDurationTime: 90,
        greenDurationTime: 70,
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
  },
}

const junctionTimeprogressSlice = createSlice({
  name: 'junctionTimeprogress',
  initialState,
  reducers: {
    setDirectionMap: (state, action) => {
      state.directionMap = action.payload || {}
    },
    setMovementMap: (state, action) => {
      state.movementMap = action.payload || {}
    },
    setTrafficState: (state, action) => {
      state.trafficState = action.payload || {}
    },
    // 部分更新（深度合并方向/车道），仅覆盖提供的字段
    mergeTrafficState: (state, action) => {
      const incoming = action.payload || {}
      Object.entries(incoming).forEach(([direction, movements]) => {
        if (!state.trafficState[direction]) state.trafficState[direction] = {}
        Object.entries(movements || {}).forEach(([movement, vals]) => {
          if (!state.trafficState[direction][movement]) {
            state.trafficState[direction][movement] = {
              redDurationTime: 0,
              greenDurationTime: 0,
              initialLight: 'red',
            }
          }
          const target = state.trafficState[direction][movement]
          if (vals.redDurationTime !== undefined)
            target.redDurationTime = vals.redDurationTime
          if (vals.greenDurationTime !== undefined)
            target.greenDurationTime = vals.greenDurationTime
          if (vals.initialLight !== undefined)
            target.initialLight = vals.initialLight
        })
      })
    },
    updateTiming: (state, action) => {
      const {
        direction,
        movement,
        redDurationTime,
        greenDurationTime,
        initialLight,
      } = action.payload || {}
      if (
        direction &&
        movement &&
        state.trafficState[direction] &&
        state.trafficState[direction][movement]
      ) {
        const target = state.trafficState[direction][movement]
        if (redDurationTime !== undefined)
          target.redDurationTime = redDurationTime
        if (greenDurationTime !== undefined)
          target.greenDurationTime = greenDurationTime
        if (initialLight !== undefined) target.initialLight = initialLight
      }
    },
  },
})

export const {
  setDirectionMap,
  setMovementMap,
  setTrafficState,
  updateTiming,
  mergeTrafficState,
} = junctionTimeprogressSlice.actions

export default junctionTimeprogressSlice.reducer
