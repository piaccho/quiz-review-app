package com.unicorns.backend.repository;

import com.unicorns.backend.model.PresetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresetEntryRepository extends JpaRepository<PresetEntry, Long> {
}
