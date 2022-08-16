export enum MedicineType {
  AfterMeal,
  Timing,
}
export enum DosageUnit {
  pill,
  ml,
  g,
}
export interface Medicine {
  name: string;
  type: MedicineType | '',
  dosage: string;
  dosageUnit: DosageUnit,
  delay?: string;
  frequency: string;
  lastNotification?: string;
  startDate: string;
  endDate: string;
  meals?: string[],
  note: string;
}
