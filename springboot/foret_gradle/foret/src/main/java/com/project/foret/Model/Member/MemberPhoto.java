package com.project.foret.Model.Member;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_photo")
@Getter
@Setter
@NoArgsConstructor
public class MemberPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dir;
    private String filename;
    private String originname;
    private String filetype;
    private int filesize;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

}
