<template>
    <div>
        <div class="env_content">
            <dig-left />
            <dig-right />
        </div>
    </div>
</template>

<script>
import digLeft from "./components/digLeft.vue";
import digRight from "./components/digRight.vue";

export default {
    components: { digLeft, digRight },
};
</script>

<style scoped>
.env_content {
    display: flex;
    justify-content: space-between;
}
</style>
