<script setup lang="ts">
import { useRegisterSW } from 'virtual:pwa-register/vue'
import {ref} from "vue";
import SpinnerIcon from '@/components/spinner.vue';

const {
  offlineReady,
  needRefresh,
  updateServiceWorker,
} = useRegisterSW({
  immediate: true,
  onRegistered(r) {
    r && setInterval(async () => {
      await r.update();
    }, 60 * 60 * 1000);
  }
});
const isRefreshing = ref<boolean>(false);

function doRefresh() {
  isRefreshing.value = true;
  updateServiceWorker();
}

const close = async () => {
  offlineReady.value = false
  needRefresh.value = false
}
</script>

<script lang="ts">
export default {
  name: 'ReloadPrompt',
}
</script>

<template lang="pug">
.pwa-toast.fixed.right-4.bottom-4.p-3.border.border-gray-200.rounded.bg-white.shadow-md(
  v-if="offlineReady || needRefresh"
  role="alert"
)
  p.message.mb-2
    span(v-if="offlineReady") 离线运行已就绪
    span(v-else-if="isRefreshing") 重启中...
    span(v-else) 新版本已就绪，点击重启
  button.border.border-gray-200.rounded.py-1.px-2(
    v-if="needRefresh",
    type="button",
    :disabled="isRefreshing",
    @click="doRefresh",
  )
    spinner-icon(v-if="isRefreshing")
    template(v-else) 更新并重启
  button.border.border-gray-200.rounded.py-1.px-2.ml-2(type="button" @click="close") 关闭
</template>
