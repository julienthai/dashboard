import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Dashboard = () => import('@/entities/dashboard/dashboard.vue');
// prettier-ignore
const DashboardUpdate = () => import('@/entities/dashboard/dashboard-update.vue');
// prettier-ignore
const DashboardDetails = () => import('@/entities/dashboard/dashboard-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/dashboard/new',
    name: 'DashboardCreate',
    component: DashboardUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/dashboard/:dashboardId/edit',
    name: 'DashboardEdit',
    component: DashboardUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/dashboard/:dashboardId/view',
    name: 'DashboardView',
    component: DashboardDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
