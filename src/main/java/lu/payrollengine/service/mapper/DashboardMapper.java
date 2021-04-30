package lu.payrollengine.service.mapper;

import lu.payrollengine.domain.*;
import lu.payrollengine.service.dto.DashboardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dashboard} and its DTO {@link DashboardDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DashboardMapper extends EntityMapper<DashboardDTO, Dashboard> {}
