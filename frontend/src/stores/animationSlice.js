import { createSlice } from "@reduxjs/toolkit";

const animationSlice = createSlice({
    name: "animation",
    initialState: {
        animationIndex: 0,
    },
    reducers: {
        setAnimationIndex: (state, action) => {
            console.log("Dispatching animationIndex:", action.payload); //
            state.animationIndex = action.payload;
        },
    },
});

export const { setAnimationIndex } = animationSlice.actions;
export default animationSlice.reducer;