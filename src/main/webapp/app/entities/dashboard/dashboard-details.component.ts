import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDashboard } from '@/shared/model/dashboard.model';
import DashboardService from './dashboard.service';

@Component
export default class DashboardDetails extends Vue {
  @Inject('dashboardService') private dashboardService: () => DashboardService;
  public dashboard: IDashboard = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dashboardId) {
        vm.retrieveDashboard(to.params.dashboardId);
      }
    });
  }

  public retrieveDashboard(dashboardId) {
    this.dashboardService()
      .find(dashboardId)
      .then(res => {
        this.dashboard = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
