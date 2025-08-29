import { createSlice } from '@reduxjs/toolkit'

// 仅存储地点与总流量 totalvolume (兼容旧字段 trafficVolume)
const initialState = {
  // 默认初始数据，可根据需要改为空数组 []
  runtimeData: [
    { location: '旺龙路', totalvolume: 1210 },
    { location: '望青路', totalvolume: 150 },
    { location: '尖山路', totalvolume: 180 },
    { location: '岳麓大道', totalvolume: 150 },
    { location: '青山路', totalvolume: 170 },
    { location: '望安路', totalvolume: 160 },
  ],
}

const flowRuntimeSlice = createSlice({
  name: 'flowRuntime',
  initialState,
  reducers: {
    // 直接用数组整体替换：window.flowRuntimeChanged([...]) 或 dispatch(setFlowRuntimeData([...]))
    setFlowRuntimeData: (state, action) => {
      const payload = action.payload
      if (!Array.isArray(payload)) {
        console.warn(
          'setFlowRuntimeData 需要数组，例如: [{ location: "旺龙路", totalvolume: 120 }, ...]'
        )
        return
      }
      state.runtimeData = payload.map((i) => ({
        location: i.location,
        totalvolume: i.totalvolume ?? i.totalVolume ?? i.trafficVolume ?? 0,
      }))
    },
  },
})

export const { setFlowRuntimeData } = flowRuntimeSlice.actions

export default flowRuntimeSlice.reducer
