import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import router from './router';
import {registerServiceWorker} from './service/notification';
import './index.css'
import './style/layout.styl'
import './style/icon.styl'

// TODO 增加浏览器兼容性检查，比如支持我们需要的 API

registerServiceWorker();

const pinia = createPinia();
const app = createApp(App);
app.use(router);
app.use(pinia);
app.mount('#app')
