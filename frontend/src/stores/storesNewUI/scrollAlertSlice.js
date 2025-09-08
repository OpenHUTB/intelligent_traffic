import { createSlice } from '@reduxjs/toolkit'

// Utility to generate a random time (HH:MM) for today
const generateRandomTimeToday = () => {
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const randomMs = Math.floor(Math.random() * (now.getTime() - today.getTime()))
  const randomTime = new Date(today.getTime() + randomMs)
  return randomTime.toLocaleTimeString('zh-CN', { hour12: false }).slice(0, 5)
}

// const initialState = {
//   listItems: [
//     {
//       name: '逆行',
//       position: 'G60 k987',
//       number: '沪A12345',
//       status: '极高',
//       isAlert: '是',
//       isDeal: '否',
//       speed: '0',
//       time: generateRandomTimeToday(),
//     },
//     {
//       name: '异常停车',
//       position: 'G60 k987',
//       number: '湘A19B155',
//       status: '高',
//       isAlert: '是',
//       isDeal: '否',
//       speed: '20',
//       time: generateRandomTimeToday(),
//     },
//     {
//       name: '超高速',
//       status: '高',
//       position: 'G60 k984',
//       number: '湘A91812',
//       isAlert: '是',
//       isDeal: '否',
//       speed: '120',
//       time: generateRandomTimeToday(),
//     },
//     {
//       name: '超低速',
//       status: '普通',
//       position: 'G60 k988',
//       number: '湘E6L123',
//       isAlert: '否',
//       isDeal: '否',
//       speed: '10',
//       time: generateRandomTimeToday(),
//     },
//     {
//       name: '占用应急车道',
//       status: '普通',
//       position: 'G60 k985',
//       number: '湘A8L877',
//       isAlert: '否',
//       isDeal: '否',
//       speed: '30',
//       time: generateRandomTimeToday(),
//     },
//   ],
// }

const initialState = {
  listItems: [],
}

const scrollAlertSlice = createSlice({
  name: 'highwayScrollAlert',
  initialState,
  reducers: {
    setHighwayScrollAlertList: (state, action) => {
      state.listItems = action.payload
    },
    addHighwayScrollAlertItem: (state, action) => {
      state.listItems.unshift(action.payload)
    },
    updateHighwayScrollAlertItem: (state, action) => {
      const { index, data } = action.payload
      if (index >= 0 && index < state.listItems.length) {
        state.listItems[index] = { ...state.listItems[index], ...data }
      }
    },
    clearHighwayScrollAlert: (state) => {
      state.listItems = []
    },
  },
})

export const {
  setHighwayScrollAlertList,
  addHighwayScrollAlertItem,
  updateHighwayScrollAlertItem,
  clearHighwayScrollAlert,
} = scrollAlertSlice.actions

export default scrollAlertSlice.reducer
