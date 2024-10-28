import {createRouter, createWebHistory} from 'vue-router';
import HomeView from "Frontend/pages/HomeView.vue";
import HelloView from "Frontend/pages/HelloView.vue";
import LoginView from "Frontend/router/LoginView.vue";

const router = createRouter({
  history: createWebHistory('/admin'),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/hello',
      name: 'hello',
      component: HelloView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    }
  ]
});

export default router;