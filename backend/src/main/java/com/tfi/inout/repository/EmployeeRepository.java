package com.tfi.inout.repository;

import com.tfi.inout.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Modifying
    @Query(value = "UPDATE employee SET active = true WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);
}