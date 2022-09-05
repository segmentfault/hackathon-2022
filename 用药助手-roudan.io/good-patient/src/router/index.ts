import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/pages/home.vue';
import EditMedicine from '@/pages/edit.vue';
import MedicineList from '@/pages/medicine-list.vue';
import About from "@/pages/about.vue";

export const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
  },
  {
    path: '/add',
    name: 'add',
    component: EditMedicine,
  },
  {
    path: '/medicines',
    name: 'list',
    component: MedicineList,
  },
  {
    path: '/medicines/:id',
    name: 'edit',
    component: EditMedicine,
  },
  {
    path: '/about',
    name: 'about',
    component: About,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
