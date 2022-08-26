<script lang="ts" setup>
import type {Medicine} from "@/types";
import {DosageUnitLabel} from "@/data";
import {toRefs} from "vue";
import {checkPermission, sendMessageAfter} from "@/service/notification";

type Props = {
  medicine: Medicine;
}

const props = defineProps<Props>();
const {medicine} = toRefs(props);

async function doCountDown() {
  if (!(await checkPermission())) {
    alert('请允许通知权限');
  }
  sendMessageAfter(10, 'hello', 'world');
}
</script>

<template lang="pug">
.current-medicine.fixed.left-4.right-4.bottom-4.flex.shadow-lg.p-4.border.border-solid.border-gray-200.rounded.bg-white
  .text-5xl.mr-4 ⏰
  .flex-1 {{medicine.name}}
    br
    | x
    strong.text-bold.mx-1 {{medicine.dosage}}
    | {{DosageUnitLabel[medicine.dosageUnit]}}
  button.bg-blue-500.text-white.text-lg.rounded.ml-4.w-28(
    type="button"
    @click="doCountDown"
  ) 开始计时
</template>
