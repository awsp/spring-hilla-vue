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
  <section>
    <button @click.prevent="increase">Increase</button>
    <button @click.prevent="decrease">Decrease</button>
  </section>
</template>

<style scoped>
div {
  padding: 2em;
}

section {
  display: flex;
  gap: .5em;
}
</style>