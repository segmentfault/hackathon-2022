<script lang="ts" setup>
import {version} from '../../package.json';
</script>

<script lang="ts">
export default {
  name: 'AboutApp',
}
</script>

<template lang="pug">
h2 关于姆伊用药助手
p

h3 版本：v{{version}}
</template>
