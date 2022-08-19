import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import router from './router';
import './index.css'
import './style/layout.styl'
import './style/icon.styl'

const pinia = createPinia();
const app = createApp(App);
app.use(router);
app.use(pinia);
app.mount('#app')
