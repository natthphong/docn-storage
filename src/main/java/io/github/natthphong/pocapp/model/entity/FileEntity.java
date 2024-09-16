package io.github.natthphong.pocapp.model.entity;



import io.github.natthphong.pocapp.model.entity.key.FileId;
import jakarta.persistence.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_file")
@IdClass(FileId.class)
public class FileEntity implements Serializable {

    @Id
    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Id
    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @Id
    @Column(name = "file_date", nullable = false)
    private LocalDate fileDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
