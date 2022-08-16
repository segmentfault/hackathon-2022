<script lang="ts" setup>
import {reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {MedicineTypeLabel, DosageUnitLabel} from "@/data";
import type {Medicine} from "@/types";
import {DosageUnit, MedicineType} from '@/types';
import {useMedicineStore} from "@/store";

const route = useRoute();
const medicineStore = useMedicineStore();
const MEALS = ['早餐', '午餐', '晚餐', '加餐'];

const message = ref<string>();
const medicine = reactive<Medicine>({
  name: '',
  type: '',
  dosage: '',
  dosageUnit: DosageUnit.pill,
  frequency: '',
  startDate: '',
  endDate: '',
  note: '',
  meals: [],
});

function doSubmit(event: Event) {
  if ((event.target as HTMLFormElement).matches(':invalid')) {
    return;
  }

  message.value = '';
  medicineStore.save(medicine, route.params.id ? Number(route.params.id) : undefined);
  message.value = '保存成功';
  // TODO 跳转到列表页
}
function onTypeChange() {
  if (medicine.type === MedicineType.AfterMeal && medicine.meals?.length === 0) {
    medicine.meals = ['09:00', '13:00', '20:00'];
  }
}
const mealsStatus = ref<boolean[]>(MEALS.map(() => true));
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
    input#name.border.rounded.w-full.outline-blue-400.leading-8.px-2(
      v-model="medicine.name"
      required
    )

  .form-group.mb-4
    label.block.mb-2(for="type") 用药时间
    .flex.items-center
      select#type.border.rounded.outline-blue-400.leading-8.w-32.h-8.mr-2.px-2(
        v-model.number="medicine.type"
        required
        @change="onTypeChange"
      )
        option(value="" disabled) 请选择
        option(v-for="(label, key) in MedicineTypeLabel" :value="key" :key="key") {{label}}

      input.border.rounded.outline-blue-400.leading-8.flex-1.h-8.px-2.w-40(
        v-model="medicine.delay"
        type="number"
        required
      )
      .ml-1.whitespace-nowrap.text-gray-600(
        v-if="medicine.type === MedicineType.AfterMeal"
      ) 分钟

  .form-group.mb-4(v-if="medicine.type === MedicineType.AfterMeal")
    label.block.mb-2 最后提醒时间
    .grid.meals-grid
      template(v-for="(item, index) in MEALS")
        input(
          type="checkbox"
          name="meal"
          v-model="mealsStatus[index]"
        )
        span {{item}}
        input.border.rounded.w-full.outline-blue-400.leading-8.px-2(
          v-model="medicine.meals[index]"
          type="time"
          :required="mealsStatus[index]"
        )

  .form-group.mb-4
    label.block.mb-2(for="dosage") 剂量
    .flex.items-center
      input#dosage.border.rounded.outline-blue-400.leading-8.px-2(
        v-model="medicine.dosage"
        type="number"
        required
      )
      select#dosage-unit.border.rounded.outline-blue-400.h-8.leading-8.px-2.ml-2(
        v-model="medicine.dosageUnit"
        required
      )
        option(v-for="(label, key) in DosageUnitLabel" :value="key" :key="key") {{label}}

  .w-full.leading-6.mb-2.bg-green-500.text-white.px-2.py-1.rounded(
    v-if="message"
  ) {{message}}
  button.btn.bg-blue-600.text-white.text-lg.rounded.w-full.h-12(
    class="hover:bg-blue-500"
    type="submit"
  ) 保存
</template>
