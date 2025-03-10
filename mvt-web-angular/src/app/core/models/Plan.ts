import {PlannedActivity} from "./PlannedActivity";

export interface Plan {
  id: string;
  name: string;
  description: string;
  imageUrl: string | null;
  plannedActivities: PlannedActivity[];
  createdAt: string;
}
