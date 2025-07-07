package com.polymath.topay.repositories;


import com.polymath.topay.models.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Teams,Long> {
    Optional<Teams> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Teams> findByMerchantsId(UUID merchantId);
}
