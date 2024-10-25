<script lang="ts" setup>
import {ref} from "vue";

const {value = 0} = defineProps<{
  value: number
}>();

const counterValue = ref<number>(value);

function increase() {
  counterValue.value++;
}

function decrease() {
  counterValue.value--;
}
</script>

<template>
  <div>
    Counter: {{ counterValue }}
  </div>
  <ASpace wrap>
    <AButton type="primary" htmlType="button" @click.prevent="increase">Increase</AButton>
    <AButton type="link" @click.prevent="decrease">Decrease</AButton>
  </ASpace>
</template>

<style scoped>
div {
  padding: .2em .5em;
}

section {
  display: flex;
  gap: .5em;
}
</style>