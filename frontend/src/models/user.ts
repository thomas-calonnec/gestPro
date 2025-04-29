export interface User {
    id: number | null;
    username: string;
    password: string | null;
    email: string;
    providerId: string;
}
