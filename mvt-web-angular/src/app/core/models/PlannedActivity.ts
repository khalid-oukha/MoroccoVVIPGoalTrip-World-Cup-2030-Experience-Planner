import {Activity} from "./Activity";

export interface PlannedActivity {
  id: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  startDate: string;
  endDate: string;
  notes?: string;
  activity: Activity;
  createdAt: string;
}
export interface PlannedActivityRequestDto {
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  startDate: string;
  endDate: string;
  notes?: string;
}
