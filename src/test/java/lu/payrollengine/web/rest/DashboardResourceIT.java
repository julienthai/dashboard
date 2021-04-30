package lu.payrollengine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;
import lu.payrollengine.IntegrationTest;
import lu.payrollengine.domain.Dashboard;
import lu.payrollengine.repository.DashboardRepository;
import lu.payrollengine.service.dto.DashboardDTO;
import lu.payrollengine.service.mapper.DashboardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity() {
        Dashboard dashboard = new Dashboard().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).order(DEFAULT_ORDER);
        return dashboard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createUpdatedEntity() {
        Dashboard dashboard = new Dashboard().title(UPDATED_TITLE).content(UPDATED_CONTENT).order(UPDATED_ORDER);
        return dashboard;
    }

    @BeforeEach
    public void initTest() {
        dashboardRepository.deleteAll();
        dashboard = createEntity();
    }

    @Test
    void createDashboard() throws Exception {
        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();
        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);
        restDashboardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate + 1);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDashboard.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    void createDashboardWithExistingId() throws Exception {
        // Create the Dashboard with an existing ID
        dashboard.setId("existing_id");
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        // Get the dashboard
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard
        Dashboard updatedDashboard = dashboardRepository.findById(dashboard.getId()).get();
        updatedDashboard.title(UPDATED_TITLE).content(UPDATED_CONTENT).order(UPDATED_ORDER);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(updatedDashboard);

        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDashboard.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    void putNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard.title(UPDATED_TITLE);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDashboard.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    void fullUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard.title(UPDATED_TITLE).content(UPDATED_CONTENT).order(UPDATED_ORDER);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDashboard.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDashboard.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    void patchNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();
        dashboard.setId(UUID.randomUUID().toString());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dashboardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.save(dashboard);

        int databaseSizeBeforeDelete = dashboardRepository.findAll().size();

        // Delete the dashboard
        restDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboard.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
