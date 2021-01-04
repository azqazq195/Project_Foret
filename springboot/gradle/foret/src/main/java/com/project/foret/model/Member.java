package com.project.foret.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;
    private String device_token;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "member_tag", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "member_region", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "region_id"))
    private List<Region> regions;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPhoto> photos;

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        tag.addMember(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.removeMember(this);
    }

    public void addRegion(Region region) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(region);
        region.addMember(this);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
        region.removeMember(this);
    }

    public void addPhoto(MemberPhoto memberPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(memberPhoto);
        memberPhoto.setMember(this);
    }

    public void removePhoto(MemberPhoto memberPhoto) {
        photos.remove(memberPhoto);
        memberPhoto.setMember(null);
    }

}
