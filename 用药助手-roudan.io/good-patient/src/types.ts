export enum MedicineType {
  // 餐后
  AfterMeal,
  // 定时
  Timing,
}
export enum DosageUnit {
  pill,
  ml,
  g,
}
export interface Medicine {
  // 药品名称
  name: string;
  // 用药类型
  type: MedicineType | '',
  // 剂量
  dosage: string;
  // 剂量单位
  dosageUnit: DosageUnit,
  // 延迟时间，比如餐后
  delay?: number;
  // 频率
  frequency: string;
  // 开始吃的时间
  startDate: string;
  // 结束吃的时间
  endDate: string;
  // 每餐的最后提醒时间
  meals?: string[],
  // 备注
  note: string;
  // 启动提示
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
