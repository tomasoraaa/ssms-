import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'
import '@/assets/css/global.css'

const app = createApp(App)

/*读取外部配置文件，确保配置加载完成后再启动应用*/
axios.get('/config.json').then(response => {
    // 设置全局配置
    app.config.globalProperties.$config = response.data;
    
    // 注册图标组件
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    
    // 加载插件并挂载应用
    app.use(router)
    app.use(ElementPlus, {
        locale: zhCn,
    })
    app.mount('#app')
}).catch(error => {
    console.error('加载配置文件失败:', error);
    // 即使配置加载失败，也尝试启动应用
    app.use(router)
    app.use(ElementPlus, {
        locale: zhCn,
    })
    app.mount('#app')
})

export const globals = app.config.globalProperties