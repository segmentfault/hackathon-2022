<script lang="ts" setup>
import {reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import AppHeader from '@/components/header.vue';
import {MedicineTypeLabel, DosageUnitLabel, createNewMedicine} from "@/data";
import type {Medicine} from "@/types";
import {MedicineType} from '@/types';
import {useMedicineStore} from "@/store";
import ToggleSwitch from "@/components/toggle.vue";
import {checkPermission} from "@/service/notification";

function initMedicine(): Medicine {
  if (!route.params.id) return createNewMedicine();
  const medicine = medicineStore.medicines[route.params.id.toString()];
  mealsStatus.value = (medicine.meals || []).map((meal: string) => !!meal);
  return medicine;
}

const route = useRoute();
const router = useRouter();
const medicineStore = useMedicineStore();
const MEALS = ['早餐', '午餐', '晚餐', '加餐'];
const mealsStatus = ref<boolean[]>(MEALS.map(() => true));

const status = ref<boolean | null>(null);
const medicine = reactive<Medicine>(initMedicine());

async function doSubmit(event: Event) {
  if (!(await checkPermission())) {
    alert('您必须开启通知权限才能使用此软件。');
    return;
  }

  if ((event.target as HTMLFormElement).matches(':invalid')) {
    return;
  }

  status.value = null;
  const { meals } = medicine;
  const data: Medicine = {
    ...medicine,
    meals: meals && meals.map((meal, index) => mealsStatus.value[index] ? meal : '')
  };
  medicineStore.save(data, route.params.id ? Number(route.params.id) : undefined);
  status.value = true;
  setTimeout(() => {
    router.push({
      name: 'list',
    }).catch(console.log);
  }, 1500);
}
function onTypeChange() {
  if (medicine.type === MedicineType.AfterMeal) {
    if (!medicine.meals || medicine.meals.length === 0 || medicine.meals.every(meal => !meal)) {
      medicine.meals = ['09:00', '13:00', '20:00', ''];
    }
    medicine.delay = medicine.delay || 30;
  }
}
</script>

<script lang="ts">
export default {
  name: 'EditMedicine',
}
</script>

<template lang="pug">
app-header(title="添加药物")
form#form.mx-10.mt-8(
  @submit.prevent="doSubmit"
)
  .form-group.mb-4
    label.block.mb-2(for="name") 药物名称
    input#name.border.rounded.w-full.outline-blue-400.leading-8.px-2(
      v-model="medicine.name"
      required
      placeholder="药物名称"
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
        v-model.number="medicine.delay"
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
        v-model.number="medicine.dosage"
        type="number"
        required
      )
      select#dosage-unit.border.rounded.outline-blue-400.h-8.leading-8.px-2.ml-2(
        v-model="medicine.dosageUnit"
        required
      )
        option(v-for="(label, key) in DosageUnitLabel" :value="key" :key="key") {{label}}

  .form-group.mb-4
    label.block.mb-2(for="note") 备注
    textarea#note.border.rounded.outline-blue-400.leading-8.px-2.w-full(
      v-model="medicine.note"
      rows="2"
      placeholder="备注"
    )

footer.sticky.bg-white.px-10.py-3.bottom-0.border-t.border-solid.border-gray-300
  .form-group.mb-2.flex.items-center
    toggle-switch(
      v-model="medicine.enabled"
      label="启用"
      toggle-id="enabled"
    )
    label.ml-2(for="enabled") 开始用药提醒
  button.text-white.text-lg.rounded.w-full.h-12(
    :class="status ? 'bg-green-700' : 'bg-blue-600 hover:bg-blue-500'"
    type="submit"
    form="form"
    :disabled="status !== null"
  ) {{status ? '保存成功' : '保存'}}

p.text-sm.mx-10.text-gray-500.mt-2 （这里可能有一些说明）
</template>
