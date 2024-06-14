import { createSlice } from "@reduxjs/toolkit";

const highwayWeatherSlice = createSlice({
    name: "highwayWeather",
    initialState: {
        windDirection: 0,
        windSpeed: 0,
        humidity: 0,
        rainVolume: 0,
    },
    reducers: {
        setHighwayWeatherSlice: (state, action) => {
            console.log("Dispatching animationIndex:", action.payload); //
            state.animationIndex = action.payload;
        },
    },
});

export const { setAnimationIndex } = highwayWeatherSlice.actions;
export default highwayWeatherSlice.reducer;