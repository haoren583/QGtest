import { createApp } from 'vue'
import { createPinia } from 'pinia'
//import './style.css'
import './style/mystyle.css'
import App from './App.vue'
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'



const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
const pinia = createPinia() // 创建 Pinia 实例






app.use(pinia)
// 注册路由
app.use(router)

app.mount('#app')





