// status.ts

import { TokenStore } from "./Mytoken";

interface StatusInit {
    Common: number;
    Doctor: number;
    Admin: number;
    isDel: number;
}

export const STATUS_INIT: StatusInit = {
    Common: 0,
    Doctor: 1,
    Admin: 2,
    isDel: 3,
};


export function isAuth(statusCode: number): boolean {
    const store = TokenStore();
    const statusNum = store.token?.status || 0;
    if ((statusNum & (1 << statusCode)) == 0) {
        return false;
    }
    return true;
}
