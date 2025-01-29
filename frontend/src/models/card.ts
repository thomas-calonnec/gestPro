import {Label} from './label';
import {CheckList} from './check-list';

export interface Card {
    id?: number;
    name: string;
    description: string;
    deadline: Date;
    hours: number;
    minutes: number;
    labels: Label[];
    checkList: CheckList[];
    isCompleted: boolean;
    isHovered?: boolean;
    isDateActivated: boolean;
}
