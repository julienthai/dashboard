/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import DashboardUpdateComponent from '@/entities/dashboard/dashboard-update.vue';
import DashboardClass from '@/entities/dashboard/dashboard-update.component';
import DashboardService from '@/entities/dashboard/dashboard.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Dashboard Management Update Component', () => {
    let wrapper: Wrapper<DashboardClass>;
    let comp: DashboardClass;
    let dashboardServiceStub: SinonStubbedInstance<DashboardService>;

    beforeEach(() => {
      dashboardServiceStub = sinon.createStubInstance<DashboardService>(DashboardService);

      wrapper = shallowMount<DashboardClass>(DashboardUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dashboardService: () => dashboardServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.dashboard = entity;
        dashboardServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dashboardServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dashboard = entity;
        dashboardServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dashboardServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDashboard = { id: 'ABC' };
        dashboardServiceStub.find.resolves(foundDashboard);
        dashboardServiceStub.retrieve.resolves([foundDashboard]);

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
