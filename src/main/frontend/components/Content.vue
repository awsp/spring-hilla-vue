<script setup lang="ts">
import {ContentService} from "Frontend/generated/endpoints";
import {ref, onMounted} from "vue";

const items = ref<string[]>([]);
const input = ref<string>('');

onMounted(async () => {
  items.value = await ContentService.findAll();
});

async function submit() {
  const value: string = input.value;

  if (value) {
    const savedValue = await ContentService.add(value);
    items.value.push(savedValue);
    input.value = '';
  }
}
</script>

<template>
  <div>
    Content
    <ol>
      <li v-for="item in items">{{ item }}</li>
    </ol>
    <form @submit.prevent="submit">
      <input type="text" v-model="input"/>
      <button type="submit">Add</button>
    </form>
  </div>
</template>

<style scoped>
div {
  padding: 2em;
}
</style>