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
  startDate: string;
  endDate: string;
  meals?: string[],
  note: string;
  enabled?: boolean;
}

export type Menu = {
  label: string;
  command: string | number;
  params?: unknown;
}

export enum MenuCommand {
  Import,
  Export,
}
