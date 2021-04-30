<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="dashboardApp.dashboard.home.createOrEditLabel" data-cy="DashboardCreateUpdateHeading">Create or edit a Dashboard</h2>
        <div>
          <div class="form-group" v-if="dashboard.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dashboard.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dashboard-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="dashboard-title"
              data-cy="title"
              :class="{ valid: !$v.dashboard.title.$invalid, invalid: $v.dashboard.title.$invalid }"
              v-model="$v.dashboard.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dashboard-content">Content</label>
            <input
              type="text"
              class="form-control"
              name="content"
              id="dashboard-content"
              data-cy="content"
              :class="{ valid: !$v.dashboard.content.$invalid, invalid: $v.dashboard.content.$invalid }"
              v-model="$v.dashboard.content.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dashboard-order">Order</label>
            <input
              type="number"
              class="form-control"
              name="order"
              id="dashboard-order"
              data-cy="order"
              :class="{ valid: !$v.dashboard.order.$invalid, invalid: $v.dashboard.order.$invalid }"
              v-model.number="$v.dashboard.order.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.dashboard.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./dashboard-update.component.ts"></script>
