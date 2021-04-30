/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DashboardComponent from '@/entities/dashboard/dashboard.vue';
import DashboardClass from '@/entities/dashboard/dashboard.component';
import DashboardService from '@/entities/dashboard/dashboard.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Dashboard Management Component', () => {
    let wrapper: Wrapper<DashboardClass>;
    let comp: DashboardClass;
    let dashboardServiceStub: SinonStubbedInstance<DashboardService>;

    beforeEach(() => {
      dashboardServiceStub = sinon.createStubInstance<DashboardService>(DashboardService);
      dashboardServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DashboardClass>(DashboardComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          dashboardService: () => dashboardServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dashboardServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllDashboards();
      await comp.$nextTick();

      // THEN
      expect(dashboardServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dashboards[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });

    it('should load a page', async () => {
      // GIVEN
      dashboardServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(dashboardServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dashboards[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      dashboardServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(dashboardServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      dashboardServiceStub.retrieve.reset();
      dashboardServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(dashboardServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.dashboards[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dashboardServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      comp.removeDashboard();
      await comp.$nextTick();

      // THEN
      expect(dashboardServiceStub.delete.called).toBeTruthy();
      expect(dashboardServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
