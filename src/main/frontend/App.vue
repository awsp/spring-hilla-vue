<script setup lang="ts">
import Counter from "Frontend/components/Counter.vue";
import Content from "Frontend/components/Content.vue";
import ReactiveStuff from "Frontend/components/ReactiveStuff.vue";
</script>

<template>
  <h1>Vue App</h1>
  <Counter :value="5"/>
  <Content/>
  <ReactiveStuff />
</template>

<style scoped>

</style>