package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberForet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberForetRepository extends JpaRepository<MemberForet, Integer> {

}
