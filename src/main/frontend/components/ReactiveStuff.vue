<script setup lang="ts">
import {ref, computed} from "vue";
import {Subscription} from "@vaadin/hilla-frontend";
import {getClockCancellable} from "Frontend/generated/ReactiveEndpoint";

const serverTime = ref<string>('');
const subscription = ref<Subscription<string> | undefined>(undefined);
const buttonText = computed<string>(() => {
  return subscription.value ? 'Stop' : 'Start';
})

async function toggleServerClock() {
  if (subscription.value) {
    subscription.value?.cancel();
    subscription.value = undefined;
  } else {
    const clockCancellable: Subscription<string> | undefined = getClockCancellable();
    subscription.value = clockCancellable;

    clockCancellable.onNext((time: string) => {
      serverTime.value = time;
    });
  }
}

</script>

<template>
  <ASpace wrap>
    Clock:
    <AInput type="text" :value="serverTime" readonly/>
    <AButton @click.prevent="toggleServerClock">
      {{ buttonText }}
    </AButton>
  </ASpace>
</template>

<style scoped>
input[type="text"] {
  width: 300px;
}
</style>