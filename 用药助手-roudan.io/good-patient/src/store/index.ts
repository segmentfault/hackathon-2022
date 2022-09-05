import {defineStore} from 'pinia';
import {computed, ref} from "vue";
import find from 'lodash/find';
import {Medicine} from "@/types";
import type {LocalStore} from "@/store/types";
import {version} from '../../package.json';
import {diffMinutes} from "@/utils";

const STORE_KEY = 'mui-medicine';

let store: LocalStore;
const local = localStorage.getItem(STORE_KEY);
if (local) {
  store = JSON.parse(local);
  // TODO 将来要处理不同版本的存储数据结构
  store.version = version;
} else {
  store = {
    version,
    medicines: {},
    config: {},
    lastId: 1,
  };
}

export const useMedicineStore = defineStore('medicine', () => {
  const data = store.medicines;
  const medicines = ref<Record<string, Medicine>>(data);

  const total = computed<number>(() => {
    return Object.values(medicines.value).length;
  });

  const save = (data: Medicine, id?: number) => {
    if (!id) {
      id = store.lastId;
      store.lastId += 1;
    }
    medicines.value[id] = data;
    store.medicines[id] = data;
    localStorage.setItem(STORE_KEY, JSON.stringify(store));
  }
  const remove = (id: string) => {
    delete medicines.value[id];
    delete store.medicines[id];
    localStorage.setItem(STORE_KEY, JSON.stringify(store));
  }
  const exportData = (): void => {
    const blob = new Blob([JSON.stringify(store)], {type: 'application/json'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'mui-medicines.json';
    a.click();
    URL.revokeObjectURL(url);
  }
  const importData = (): void => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'application/json';
    input.onchange = () => {
      const file = (input.files as FileList)[0];
      if (!file) {
        return;
      }
      const reader = new FileReader();
      reader.onload = (e) => {
        const result: string = reader.result as string;
        medicines.value = JSON.parse(result).medicines;
        localStorage.setItem(STORE_KEY, result);
      }
      reader.readAsText(file);
    }
    input.click();
  }
  const getCurrentMedicine = (): Medicine | undefined => {
    const now = new Date();
    const hour = now.getHours();
    const minute = now.getMinutes();
    return find(medicines.value, (medicine: Medicine) => {
      const {meals} = medicine;
      if (!meals) {
        return false;
      }
      // 找出1.5小时内最后提醒的药
      return !!meals.find((meal) => {
        if (!meals) return false;
        const diff = diffMinutes(hour, minute, meal);
        return diff > 0 && diff < 90;
      });
    });
  }

  return {
    medicines,
    total,
    save,
    remove,
    exportData,
    importData,
    getCurrentMedicine,
  };
});
