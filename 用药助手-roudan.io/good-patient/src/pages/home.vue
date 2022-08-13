<script lang="ts" setup>

</script>

<script lang="ts">
export default {
  name: 'AppHome',
}
</script>

<template lang="pug">
.container.flex.flex-col.justify-center.items-center.min-h-full
  figure
    img(src="../assets/logo.png")
    figcaption.text-center.text-lg.font-light.mt-4.mb-12 姆伊用药助手

  router-link.w-44.h-12.leading-12.bg-blue-500.text-white.rounded.mb-3.text-center(
    class="hover:bg-blue-600"
    :to="{name: 'add'}"
  ) 添加药物

  button.w-44.h-12.bg-green-500.text-white.rounded.mb-3(
    class="hover:bg-green-600"
    type="button"
  ) 药物列表

  button.w-44.h-12.bg-purple-500.text-white.rounded(
    class="hover:bg-purple-600"
    type="button"
  ) 用药记录
</template>
