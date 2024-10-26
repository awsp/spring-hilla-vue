<script setup lang="ts">
import {RouterView} from 'vue-router'
</script>

<template>
  <h1>Vue App</h1>

  <nav>
    <ASpace wrap>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/hello">Hello</RouterLink>
    </ASpace>
  </nav>

  <RouterView/>
</template>

<style scoped>
  nav {
    padding: 1em .5em;
  }
</style>