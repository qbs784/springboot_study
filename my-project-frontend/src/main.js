// 导入 createApp 函数，用于创建 Vue 应用实例
import { createApp } from 'vue'
// 导入根组件 App.vue
import App from './App.vue'
// 导入路由配置
import router from './router'
// 导入 axios 库，用于发起 HTTP 请求
import axios from "axios";

// 导入 Element Plus 的深色主题 CSS 变量文件
import 'element-plus/theme-chalk/dark/css-vars.css'

// 设置 axios 的默认基础 URL
axios.defaults.baseURL = 'http://localhost:8080'

// 创建 Vue 应用实例并挂载到 #app 元素上
const app = createApp(App)

// 使用路由
app.use(router)

// 挂载应用
app.mount('#app')
