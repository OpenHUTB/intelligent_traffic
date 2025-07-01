// scrollAlertSlice.js

import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    listItems: [
        {
            name: `在青山路与岳麓西大道交叉口,西方向左转  红灯增加3秒,绿灯减少1秒;直行绿灯增加10秒,红灯减少5秒`,
            time: '09:12:34',
        },
        {
            name: `在岳麓西大道与旺龙路交叉口，南向北方向左转  红灯增加2秒，绿灯减少2秒；直行绿灯增加8秒，红灯减少4秒`,
            time: '09:15:31',
        },
        {
            name: `在旺龙路与青山路交叉口，西向东方向左转 红灯增加4秒，绿灯减少2秒；直行绿灯增加12秒，红灯减少6秒`,
            time: '09:22:11',
        },
        {
            name: `在青山路与旺龙路交叉口，北向南方向左转 红灯增加5秒，绿灯减少3秒；直行绿灯增加15秒，红灯减少7秒`,
            time: '09:23:21',
        },
        {
            name: `在岳麓西大道与青山路交叉口，东向西方向左转 红灯增加1秒，绿灯减少1秒；直行绿灯增加6秒，红灯减少3秒`,
            time: '09:25:31',
        },
    ],
};

const scrollAlertSlice = createSlice({
    name: 'scrollAlert',
    initialState,
    reducers: {
        setListItems: (state, action) => {
            state.listItems = action.payload;
        },
    },
});

export const { setListItems } = scrollAlertSlice.actions;

export default scrollAlertSlice.reducer;