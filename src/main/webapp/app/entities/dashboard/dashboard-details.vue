<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dashboard">
        <h2 class="jh-entity-heading" data-cy="dashboardDetailsHeading"><span>Dashboard</span> {{ dashboard.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ dashboard.title }}</span>
          </dd>
          <dt>
            <span>Content</span>
          </dt>
          <dd>
            <span>{{ dashboard.content }}</span>
          </dd>
          <dt>
            <span>Order</span>
          </dt>
          <dd>
            <span>{{ dashboard.order }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="dashboard.id"
          :to="{ name: 'DashboardEdit', params: { dashboardId: dashboard.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dashboard-details.component.ts"></script>
