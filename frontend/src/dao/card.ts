import {Label} from './label';
import {CheckList} from './check-list';

export interface Card {
    id: number;
    name: string;
    description: string;
    deadline: Date;
    labels: Label[];
    checkList: CheckList[];
}
