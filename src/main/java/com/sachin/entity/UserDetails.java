package com.sachin.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_details")
public class UserDetails {
    @Id
    private String email;
    private String name;
    private String phone;
    private String state;
    private String gender;
    private String photos;

}
