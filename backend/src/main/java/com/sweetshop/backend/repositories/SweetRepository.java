package com.sweetshop.backend.repositories;

import com.sweetshop.backend.models.Sweet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SweetRepository extends JpaRepository<Sweet, Long> {
	
	List<Sweet> findByNameContainingIgnoreCase(String name);
	List<Sweet> findByCategoryContainingIgnoreCase(String category);
	boolean existsByNameIgnoreCase(String name);

}

