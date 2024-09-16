package io.github.natthphong.pocapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_company")
public class CompanyEntity implements Serializable {

    @Id
    @Column(name = "company_code", length = 50)
    private String companyCode;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "company_description", length = 255)
    private String companyDescription;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_by", length = 50)
    private String createBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by", length = 50)
    private String updateBy;

    @Column(name = "is_deleted", length = 1, columnDefinition = "char(1) default 'N'")
    private String isDeleted;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }


}
