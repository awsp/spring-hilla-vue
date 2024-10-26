<script setup lang="ts">
import {useAppStore} from "Frontend/store";
import {storeToRefs} from "pinia";

const appStore = useAppStore();
const {increment, decrement} = appStore;
const {counter} = storeToRefs(appStore);
</script>

<template>
  <h2>This is hello view</h2>
  <p>Counter using state management</p>
  <ASpace wrap>
    <AButton @click.prevent="increment">+</Abutton>
    <span>
    {{ counter }}
    </span>
    <AButton @click.prevent="decrement">-</AButton>
  </ASpace>
</template>

<style scoped>
span {
  display: inline-block;
  text-align: center;
  width: 30px;
}
</style>