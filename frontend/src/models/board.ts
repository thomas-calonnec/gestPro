export interface Board {
    id?: number;
    name: string;
    color: string;
    description: string;
    lastUpdated: Date;
    cardCount: number;
    weeksSinceUpdate: number;
    daysSinceUpdate: number;
    hoursSinceUpdate: number;
    minutesSinceUpdate: number;
    secondsSinceUpdate: number;
}
