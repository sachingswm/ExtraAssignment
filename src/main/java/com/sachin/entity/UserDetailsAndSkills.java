package com.sachin.entity;

import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetailsAndSkills {
    private String email;
    private String name;
    @Size(min=10,max=10,message="Phone number must be of 10 digits")
    private String phone;
    private String state;
    private String gender;
    private String photos;
    private List<String> skills;

    public UserDetailsAndSkills getUserDetailsAndSkills(UserDetails userDetails,List<String> skills)
    {
        return new UserDetailsAndSkills(userDetails.getEmail()
                ,userDetails.getName()
                ,userDetails.getPhone()
                ,userDetails.getState(),userDetails.getGender(),userDetails.getPhotos(),skills);
    }

    public UserDetails getUserDetails()
    {
        return new UserDetails(this.email,this.name,this.phone,this.state,this.gender,this.photos);
    }
}
