package com.kjm_sports.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kjm_sports.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {}