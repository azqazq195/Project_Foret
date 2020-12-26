package com.project.foret.Repository.Member;

import com.project.foret.Model.Member.MemberPhoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Integer> {

}
