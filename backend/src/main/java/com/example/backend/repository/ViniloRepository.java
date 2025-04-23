package com.example.backend.repository;

import com.example.backend.model.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViniloRepository extends JpaRepository<Vinilo, Long> {
}
