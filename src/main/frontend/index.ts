import {createApp} from 'vue'
import './style.css'
// @ts-ignore
import App from './App.vue';
import Antd from 'ant-design-vue';

const app = createApp(App);
app.use(Antd).mount('#outlet')
