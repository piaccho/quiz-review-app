package com.unicorns.backend.repository;

import com.unicorns.backend.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrizeRepository extends JpaRepository<Prize, Long> {
}
