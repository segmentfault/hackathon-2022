import {DosageUnit, Medicine, MedicineType} from "@/types";

export const MedicineTypeLabel = {
  [MedicineType.AfterMeal]: '餐后',
  [MedicineType.Timing]: '定时',
}

export const DosageUnitLabel = {
  [DosageUnit.pill]: '片',
  [DosageUnit.ml]: '毫升',
  [DosageUnit.g]: '克',
}

export function createNewMedicine(): Medicine {
  return {
    name: '',
    type: '',
    dosage: '',
    dosageUnit: DosageUnit.pill,
    frequency: '',
    startDate: '',
    endDate: '',
    note: '',
    meals: ['', '', '', ''],
    enabled: true,
  };
}

let _counter = 0;
export const counter = {
  get() {
    return _counter;
  },
  next() {
    return _counter++;
  },
}
