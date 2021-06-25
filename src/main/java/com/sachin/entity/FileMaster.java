package com.sachin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="file_master")
@Getter
@Setter
@ToString
public class FileMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileDirectory;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private LocalDateTime updated_at;
    private String userDetailsId;
}
