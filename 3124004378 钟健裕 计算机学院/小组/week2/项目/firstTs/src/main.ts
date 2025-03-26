import { createApp } from 'vue'
//import './style/style.css'
import App from './App.vue'
import './style/mystyle.css'
import router from './router'

const app = createApp(App)
app.use(router)//使用路由实例
app.mount('#app')

