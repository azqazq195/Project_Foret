package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberLikeComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLikeCommentRepository extends JpaRepository<MemberLikeComment, Integer> {

}
