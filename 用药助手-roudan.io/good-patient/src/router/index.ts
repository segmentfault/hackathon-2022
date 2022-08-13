import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/pages/home.vue';
import AddMedicine from '@/pages/add.vue';

export const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
  },
  {
    path: '/add',
    name: 'add',
    component: AddMedicine,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
