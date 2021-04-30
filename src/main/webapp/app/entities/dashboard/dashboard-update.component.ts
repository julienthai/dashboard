import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDashboard, Dashboard } from '@/shared/model/dashboard.model';
import DashboardService from './dashboard.service';

const validations: any = {
  dashboard: {
    title: {},
    content: {},
    order: {},
  },
};

@Component({
  validations,
})
export default class DashboardUpdate extends Vue {
  @Inject('dashboardService') private dashboardService: () => DashboardService;
  public dashboard: IDashboard = new Dashboard();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dashboardId) {
        vm.retrieveDashboard(to.params.dashboardId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.dashboard.id) {
      this.dashboardService()
        .update(this.dashboard)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Dashboard is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.dashboardService()
        .create(this.dashboard)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Dashboard is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveDashboard(dashboardId): void {
    this.dashboardService()
      .find(dashboardId)
      .then(res => {
        this.dashboard = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
