import type {Medicine} from "@/types";

export interface LocalStore {
  version: string;
  medicines: Record<string, Medicine>;
  lastId: number;
  config: Record<string, string>;
}
