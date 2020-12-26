package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberLikeBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLikeBoardRepository extends JpaRepository<MemberLikeBoard, Integer> {

}
