<script lang="ts" setup>
import {MedicineType} from '@/types';
import AppHeader from '@/components/header.vue';
import {useMedicineStore} from "@/store";

const medicineStore = useMedicineStore();

</script>

<template lang="pug">
app-header(title="药物列表")

.medicine-list.p-4
  .medicine-item.border.border-solid.border-gray-200.rounded.py-2.px-4.mb-4.font-light(
    v-for="(item, id) in medicineStore.medicines"
   :key="id"
  )
    header.flex.justify-between
      h3.text-lg.font-bold.mb-2 {{item.name}}
      router-link.block.w-8.h-8.leading-8.rounded.text-center(
        class="hover:bg-gray-100 -mr-2"
        :to="{name: 'edit', params: {id: id}}"
      )
        i.bi.bi-pencil
    template(v-if="item.type === MedicineType.AfterMeal")
      p.mb-2 餐后
        strong.text-green-700.font-bold.mx-1 {{item.delay}}
        | 分钟
      h4.font-normal 最后提醒时间
      .flex.justify-between
        span(v-for="meal in item.meals", :key="meal") {{meal}}

  router-link.block.bg-blue-600.text-white.text-lg.rounded.w-full.h-12.leading-12.text-center(
    class="hover:bg-blue-500"
    :to="{name: 'add'}"
  )
    i.bi.bi-plus-lg.mr-1
    | 添加药物
</template>
