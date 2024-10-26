<script setup lang="ts">

import Counter from "Frontend/components/Counter.vue";
import ReactiveStuff from "Frontend/components/ReactiveStuff.vue";
import Content from "Frontend/components/Content.vue";
</script>

<template>
  <Counter :value="5"/>
  <Content/>
  <ReactiveStuff />
</template>