package io.github.natthphong.pocapp.model.entity.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileId implements Serializable {

    private String employeeId;
    private String companyCode;
    private LocalDate fileDate;
}
