package lu.payrollengine.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link lu.payrollengine.domain.Dashboard} entity.
 */
public class DashboardDTO implements Serializable {

    private String id;

    private String title;

    private String content;

    private Integer order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DashboardDTO)) {
            return false;
        }

        DashboardDTO dashboardDTO = (DashboardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dashboardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DashboardDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
