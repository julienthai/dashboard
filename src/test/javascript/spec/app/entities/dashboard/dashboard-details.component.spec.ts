/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DashboardDetailComponent from '@/entities/dashboard/dashboard-details.vue';
import DashboardClass from '@/entities/dashboard/dashboard-details.component';
import DashboardService from '@/entities/dashboard/dashboard.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Dashboard Management Detail Component', () => {
    let wrapper: Wrapper<DashboardClass>;
    let comp: DashboardClass;
    let dashboardServiceStub: SinonStubbedInstance<DashboardService>;

    beforeEach(() => {
      dashboardServiceStub = sinon.createStubInstance<DashboardService>(DashboardService);

      wrapper = shallowMount<DashboardClass>(DashboardDetailComponent, {
        store,
        localVue,
        router,
        provide: { dashboardService: () => dashboardServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDashboard = { id: 'ABC' };
        dashboardServiceStub.find.resolves(foundDashboard);

        // WHEN
        comp.retrieveDashboard('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.dashboard).toBe(foundDashboard);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDashboard = { id: 'ABC' };
        dashboardServiceStub.find.resolves(foundDashboard);

        // WHEN
        comp.beforeRouteEnter({ params: { dashboardId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dashboard).toBe(foundDashboard);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
