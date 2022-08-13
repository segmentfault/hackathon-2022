<script lang="ts" setup>
import {ref} from "vue";
import {MedicineTypeLabel} from "@/data";
import type {Medicine} from "@/types";

const medicine = ref<Medicine>({
  name: '',
  type: '',
  dosage: '',
  frequency: '',
  startDate: '',
  endDate: '',
  note: '',
});
function doSubmit() {

}
</script>

<template lang="pug">
header.flex.border-b.items-center.justify-between.px-4.py-2.bg-gray-100.shadow-md
  img.w-9.h-9.mr-2(src="../assets/logo.png")
  h2.text-lg 添加药物
  router-link.ml-auto.text-blue-500.text-sm(
    class="hover:text-blue-600"
    :to="{name: 'home'}"
  ) 返回首页

form.mx-10.mt-8(
  @submit.prevent="doSubmit"
)
  .form-group.mb-4
    label.block.mb-2(for="name") 药物名称
    input#name.border.rounded.w-full.outline-blue-400.leading-8(
      v-model="medicine.name"
      required,
    )

  .form-group.mb-4
    label.block.mb-2(for="type") 用餐时间
    .flex
      select#type.border.rounded.outline-blue-400.leading-8.w-32.h-8.mr-2(
        v-model="medicine.type"
        required,
      )
        option(value="" disabled) 请选择
        option(v-for="(label, key) in MedicineTypeLabel" :value="key" :key="key") {{label}}

      input.border.rounded.outline-blue-400.leading-8.flex-1.h-8(
        v-model="medicine.delay"
      )

  .form-group.mb-4
    label.block.mb-2(for="last-notification") 最后提醒时间
    input#last-notification.border.rounded.w-full.outline-blue-400.leading-8(
      v-model="medicine.lastNotification"
      type="time"
      required
    )

  .form-group.mb-4
    label.block.mb-2(for="dosage") 剂量
    input#name.border.rounded.w-full.outline-blue-400.leading-8(
      v-model="medicine.dosage"
      type="number"
      required
    )
</template>
