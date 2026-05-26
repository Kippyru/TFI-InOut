package com.tfi.inout.repository;

import com.tfi.inout.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "UPDATE user SET active = true WHERE id = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);
}