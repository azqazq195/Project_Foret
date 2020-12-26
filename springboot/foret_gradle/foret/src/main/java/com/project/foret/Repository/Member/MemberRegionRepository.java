package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberRegion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRegionRepository extends JpaRepository<MemberRegion, Integer> {

}
