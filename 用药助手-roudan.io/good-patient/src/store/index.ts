import { defineStore } from 'pinia';
import {computed, ref} from "vue";
import {Medicine} from "@/types";

const MEDICINE_KEY = 'mui-medicine';
const LAST_MEDICINE_ID = 'mui-last-medicine-id';

function getLocal<T>(key: string, defaultValue: T): T {
  const local = localStorage.getItem(key);
  if (local) {
    return JSON.parse(local);
  } else {
    return defaultValue;
  }
}

export const useMedicineStore = defineStore('medicine', () => {
  const data = getLocal<Record<string, Medicine>>(MEDICINE_KEY, {});
  const lastId = getLocal<number>(LAST_MEDICINE_ID, 1);
  const medicines = ref<Record<string, Medicine>>(data);

  const save = (data: Medicine, id?: number) => {
    id = id || lastId;
    medicines.value[id] = data;
    localStorage.setItem(MEDICINE_KEY, JSON.stringify(medicines.value));
  }
  const remove = (id: string) => {
    delete medicines.value[id];
    localStorage.setItem(MEDICINE_KEY, JSON.stringify(medicines.value));
  }

  const total = computed<number>(() => {
    return Object.values(medicines.value).length;
  })

  return { medicines, total, save, remove };
});
