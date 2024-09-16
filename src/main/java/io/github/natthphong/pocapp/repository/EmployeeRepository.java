package io.github.natthphong.pocapp.repository;

import io.github.natthphong.pocapp.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity e WHERE FUNCTION('DATE', e.createDate) = :currentDate AND e.company.companyCode = :companyCode")
    List<EmployeeEntity> findAllByCreateDateAndCompanyCode(
            @Param("currentDate") LocalDate currentDate,
            @Param("companyCode") String companyCode
    );
}
