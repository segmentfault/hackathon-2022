export enum MedicineType {
  AfterMeal,
  Timing,
}
export interface Medicine {
  name: string;
  type: MedicineType | '',
  dosage: string;
  delay: string;
  frequency: string;
  lastNotification: string;
  startDate: string;
  endDate: string;
  note: string;
}
