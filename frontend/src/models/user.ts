import {Board} from '@models/board';

export interface User {
    id: number | null;
    username: string;
    password: string ;
    email: string;
    providerId: string;
    pictureUrl: string;
    boards: Board[];
}
