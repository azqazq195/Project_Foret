package com.project.foret.Model.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_region")
@Getter
@Setter
@NoArgsConstructor
public class MemberRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String region_si;
    private String region_gu;

    @Builder
    public MemberRegion(String region_si, String region_gu) {
        this.region_si = region_si;
        this.region_gu = region_gu;
    }
}
