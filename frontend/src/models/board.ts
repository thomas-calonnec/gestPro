import {User} from '@models/user';
import {ListCard} from '@models/list-card';

export interface Board {
    id?: number;
    name: string;
    color: string;
    description: string;
    lastUpdated: Date;
    cardCount: number;
    ownerId: number;
    members: User[];
    listCards?: ListCard[],
    status: string;
    weeksSinceUpdate: number;
    daysSinceUpdate: number;
    hoursSinceUpdate: number;
    minutesSinceUpdate: number;
    secondsSinceUpdate: number;
}
