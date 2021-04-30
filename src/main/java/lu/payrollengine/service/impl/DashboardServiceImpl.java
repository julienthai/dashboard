package lu.payrollengine.service.impl;

import java.util.Optional;
import lu.payrollengine.domain.Dashboard;
import lu.payrollengine.repository.DashboardRepository;
import lu.payrollengine.service.DashboardService;
import lu.payrollengine.service.dto.DashboardDTO;
import lu.payrollengine.service.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Dashboard}.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardDTO save(DashboardDTO dashboardDTO) {
        log.debug("Request to save Dashboard : {}", dashboardDTO);
        Dashboard dashboard = dashboardMapper.toEntity(dashboardDTO);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    @Override
    public Optional<DashboardDTO> partialUpdate(DashboardDTO dashboardDTO) {
        log.debug("Request to partially update Dashboard : {}", dashboardDTO);

        return dashboardRepository
            .findById(dashboardDTO.getId())
            .map(
                existingDashboard -> {
                    dashboardMapper.partialUpdate(existingDashboard, dashboardDTO);
                    return existingDashboard;
                }
            )
            .map(dashboardRepository::save)
            .map(dashboardMapper::toDto);
    }

    @Override
    public Page<DashboardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dashboards");
        return dashboardRepository.findAll(pageable).map(dashboardMapper::toDto);
    }

    @Override
    public Optional<DashboardDTO> findOne(String id) {
        log.debug("Request to get Dashboard : {}", id);
        return dashboardRepository.findById(id).map(dashboardMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
    }
}
