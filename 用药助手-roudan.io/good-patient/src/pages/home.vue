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
      img(src="../assets/logo.png")
      figcaption.text-center.text-lg.font-light.mt-4.mb-12 姆伊用药助手

  router-link.w-44.h-12.leading-12.bg-blue-500.text-white.rounded.mb-3.text-center(
    class="hover:bg-blue-600"
    :to="{name: 'add'}"
  ) 💊 添加药物

  router-link.w-44.h-12.leading-12.bg-green-500.text-white.rounded.mb-3.text-center(
    class="hover:bg-green-600"
    :to="{name: 'list'}"
  ) 药物列表
    span.ml-1(v-if="store.total") x{{store.total}}

  button.w-44.h-12.bg-purple-500.text-white.rounded(
    class="hover:bg-purple-600"
    type="button"
  ) 📅 用药记录
</template>
