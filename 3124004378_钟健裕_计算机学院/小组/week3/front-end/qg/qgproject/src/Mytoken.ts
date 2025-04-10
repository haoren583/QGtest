import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

//定义token的类型
export interface Token {
    access_token: string
    userId: number
    status: number
    userName: string
}

//存放token的地方
export const TokenStore = defineStore('TokenStore', () => {
    const tokenPackage = ref<Token | null>(null)
    const token = computed<Token>(() => {
        if (tokenPackage.value) {
            return tokenPackage.value
        } 
        //从localStorage中获取token
        const localToken = window.localStorage.getItem("localToken");
        if (localToken) {
            //从本地存储的token中解析出token的JSON对象
            const tokenJson = JSON.parse(localToken);
            //将token的JSON对象赋值给tokenPackage
            tokenPackage.value = tokenJson;
            return tokenJson;
        }
        //如果没有token，则返回空null
        return null;
    })

    //设置token
    function saveToken(data: string) {
        //判断token是否为base64URL格式
        if (!/^[A-Za-z0-9-_=]+\.[A-Za-z0-9-_=]+\.?[A-Za-z0-9-_.+/=]*$/.test(data)) {
            window.localStorage.removeItem("localToken");
            tokenPackage.value = null;
            return;
        }
        let TokenJson: Token = {
            access_token: '',
            userId: 0,
            status: 0,
            userName: ''
        }
        TokenJson.access_token = data;
        // 对data进行Base64URL到Base64的转换
        let tokenString = data.replace(/-/g, '+').replace(/_/g, '/');
        // 如果长度不是4的倍数，则在末尾添加等号
        while (tokenString.length % 4 !== 0) {
            tokenString += '=';
        }
        // 解码base64
        const tokenPayloadBase64 = tokenString.split('.')[1];
        const tokenPayloadUint8Array = Uint8Array.from(atob(tokenPayloadBase64), c => c.charCodeAt(0));
        const tokenPayload = JSON.parse(new TextDecoder().decode(tokenPayloadUint8Array));
        // 解码base64
        //const tokenPayload = JSON.parse(atob(tokenString.split('.')[1]));
        TokenJson.userId = tokenPayload.userId;
        TokenJson.status = tokenPayload.status;
        TokenJson.userName = tokenPayload.userName;
        // 将token的JSON对象赋值给tokenPackage
        tokenPackage.value = TokenJson;
        // 将token存入localStorage
        window.localStorage.setItem("localToken", JSON.stringify(TokenJson));
    }

    return { token, saveToken };
})