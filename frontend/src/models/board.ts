import {User} from '@models/user';

export interface Board {
    id?: number;
    name: string;
    color: string;
    description: string;
    lastUpdated: Date;
    cardCount: number;
    ownerId: number;
    members: User[];
    status: string;
    weeksSinceUpdate: number;
    daysSinceUpdate: number;
    hoursSinceUpdate: number;
    minutesSinceUpdate: number;
    secondsSinceUpdate: number;
}
