package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Integer> {

}
