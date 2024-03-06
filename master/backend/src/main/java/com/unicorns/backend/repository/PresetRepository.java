package com.unicorns.backend.repository;

import com.unicorns.backend.model.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresetRepository extends JpaRepository<Preset, Long> {

}
