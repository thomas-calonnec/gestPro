import {Board} from '@models/board';

export interface Workspace {
  id: number;
  name: string;
  description: string;
  updateAt: Date;
  weeksSinceUpdate: number;
  daysSinceUpdate: number;
  hoursSinceUpdate: number;
  minutesSinceUpdate: number;
  secondsSinceUpdate: number;
  isFavorite: boolean;
  boards: Board[];
}
