<script lang="ts" setup>
import {toRefs} from "vue";

interface Props {
  title: string;
}
const props = withDefaults(defineProps<Props>(), {
  title: '姆伊用药助手',
});
const { title } = toRefs(props);
</script>

<script lang="ts">
export default {
  name: 'AppHeader',
}
</script>

<template lang="pug">
header.flex.border-b.items-center.justify-between.px-4.py-2.bg-gray-100.shadow-md.sticky.w-full.top-0
  img.w-9.h-9.mr-2(src="../assets/logo.png")
  h2.text-lg {{title}}
  router-link.ml-auto.text-blue-500.text-sm(
    class="hover:text-blue-600"
    :to="{name: 'home'}"
  ) 返回首页
</template>
