package com.project.foret.Repository;

import com.project.foret.Model.MemberTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Integer> {

}
