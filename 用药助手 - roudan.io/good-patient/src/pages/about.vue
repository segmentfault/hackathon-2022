<script lang="ts" setup>
import AppHeader from '@/components/header.vue';
import {version} from '../../package.json';
</script>

<script lang="ts">
export default {
  name: 'AboutApp',
}
</script>

<template lang="pug">
app-header(
  title="关于"
)
.flex.flex-col.items-center
  h2 姆伊用药助手
  p

  h3 版本：v{{version}}
</template>
