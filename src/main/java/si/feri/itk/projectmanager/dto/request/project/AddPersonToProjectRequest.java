package si.feri.itk.projectmanager.dto.request.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AddPersonToProjectRequest implements IDuration {
    @NotNull
    private UUID personId;

    @NotNull
    private LocalDate from;
    //if null person till project end
    private LocalDate to;

    @NotNull
    @DecimalMax(value = "1.000")
    @DecimalMin(value = "0.001")
    private BigDecimal estimatedPm;

    @JsonIgnore
    @Override
    public LocalDate getStartDate() {
        return from;
    }

    @JsonIgnore
    @Override
    public LocalDate getEndDate() {
        return to;
    }
}
