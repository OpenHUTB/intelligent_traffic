<template>
  <div>
    <div class="center">
      <left-content :message="message" />
      <center-map :message="message" @update-message="updateMessage" />
      <right-content :message="message"/>
    </div>
    <div class="bottom">
      <bottom :message="message"/>
    </div>
  </div>
</template>

<script>
import bottom from './component/bottom.vue';
import centerMap from "./component/centerMap.vue";
import leftContent from "./component/leftContent.vue";
import rightContent from "./component/rightContent.vue";
export default {
  components: { centerMap, leftContent, rightContent,bottom },
  data() {
    return {
      message: ''
    };
  },
  methods: {
    updateMessage(newMessage) {
      // console.log(newMessage);
      this.message = newMessage;
      // console.log(this.message);
    }
  }
};
</script>

<style lang="scss" scoped>
.center {
  display: flex;
  justify-content: space-between;
}
.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
</style>
