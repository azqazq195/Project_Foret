package com.project.foret.repository;

import com.project.foret.model.Region;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByRegionSiAndRegionGu(String regionSi, String regionGu);
}
