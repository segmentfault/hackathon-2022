<script lang="ts" setup>
import {useMedicineStore} from "@/store";

const store = useMedicineStore();
</script>

<script lang="ts">
export default {
  name: 'AppHome',
}
</script>

<template lang="pug">
.flex.flex-col.justify-center.items-center.h-full
  router-link(
    :to="{name: 'about'}"
  )
    figure
      img.w-44.h-44.rounded-full.drop-shadow(src="/icons/icon-512x512.png")
      figcaption.text-center.text-lg.font-light.mt-4.mb-12 姆伊用药助手

  router-link.w-44.h-12.leading-12.bg-blue-500.text-white.rounded.mb-3.text-center(
    class="hover:bg-blue-600"
    :to="{name: 'add'}"
  ) 💊 添加药物

  router-link.w-44.h-12.leading-12.bg-green-500.text-white.rounded.mb-3.flex.justify-center.items-center(
    class="hover:bg-green-600"
    :to="{name: 'list'}"
  )
    .mr-1.bg-white.rounded-sm.w-4.h-4.text-green-500.text-xs.text-center.font-bold {{store.total}}
    | 正在用药


  button.w-44.h-12.bg-purple-500.text-white.rounded(
    class="hover:bg-purple-600"
    type="button"
  ) 📅 用药记录
</template>
