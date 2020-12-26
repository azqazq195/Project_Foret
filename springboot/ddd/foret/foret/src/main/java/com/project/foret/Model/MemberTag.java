package com.project.foret.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_tag")
@Getter
@Setter
@NoArgsConstructor
public class MemberTag {
    private String tag_name;

    @Builder
    public MemberTag(String tag_name) {
        this.tag_name = tag_name;
    }
}
