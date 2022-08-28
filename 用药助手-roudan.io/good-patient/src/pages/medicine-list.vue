<script lang="ts" setup>
import type {Medicine, Menu} from '@/types';
import {MedicineType, MenuCommand} from '@/types';
import AppHeader from '@/components/header.vue';
import {useMedicineStore} from "@/store";

const medicineStore = useMedicineStore();

const menu: Menu[] = [
  {
    label: '导出数据',
    command: MenuCommand.Export,
  },
  {
    label: '导入数据',
    command: MenuCommand.Import,
  },
];

function doRemove(item: Medicine, id: string) {
  if (!confirm(`您确定要删除 ${item.name} 吗？`)) {
    return;
  }
  medicineStore.remove(id);
}
function onClickMenu(command: string | number) {
  switch (command) {
    case MenuCommand.Import:
      medicineStore.importData();
      break;

    case MenuCommand.Export:
      medicineStore.exportData();
      break;
  }
}
</script>

<template lang="pug">
app-header(
  title="药物列表"
  :menu="menu"
  @click-menu="onClickMenu"
)

.medicine-list.p-4.pb-0(v-if="medicineStore.total")
  .medicine-item.border.border-solid.border-gray-200.rounded.py-2.px-4.mb-4.font-light(
    v-for="(item, id) in medicineStore.medicines"
   :key="id"
  )
    header.flex
      h3.text-lg.font-bold.mb-2 {{item.name}}
      button.w-8.h-8.p-0.rounded.ml-auto(
        type="button"
        @click="doRemove(item, id)"
      )
        i.bi.bi-trash
      router-link.block.w-8.h-8.leading-8.rounded.text-center.ml-2(
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
        span.font-mono(v-for="meal in item.meals", :key="meal") {{meal}}

.h-40.flex.justify-center.items-center.flex-col.text-gray-400(v-else)
  i.text-6xl.bi.bi-inbox
  span.text-xs 尚未添加药物

router-link.block.bg-blue-600.text-white.text-lg.rounded.h-12.leading-12.text-center.mx-4(
  class="hover:bg-blue-500"
  :to="{name: 'add'}"
)
  i.bi.bi-plus-lg.mr-1
  | 添加药物
</template>
