package com.tfi.inout.repository;

import com.tfi.inout.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Modifying
    @Query(value = "UPDATE role SET active = true WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);

    java.util.Optional<Role> findByName(String name);
}