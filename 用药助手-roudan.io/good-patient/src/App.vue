<script setup lang="ts">
import ReloadPrompt from '@/components/reload-prompt.vue';
</script>

<template lang="pug">
.container.mx-auto.h-full
  router-view

reload-prompt
</template>
