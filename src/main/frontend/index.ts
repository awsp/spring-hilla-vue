import {createApp} from 'vue'
import './style.css'
// @ts-ignore
import App from './App.vue';
// @ts-ignore
import router from './router/index';
import Antd from 'ant-design-vue';
import {createPinia} from 'pinia'


const app = createApp(App);
app.use(Antd)
app.use(router);
app.use(createPinia())

app.mount('#outlet');
