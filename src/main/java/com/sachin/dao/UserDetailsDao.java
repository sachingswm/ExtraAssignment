package com.sachin.dao;

import com.sachin.entity.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsDao extends CrudRepository<UserDetails,String> {
    public UserDetails getUserDetailsByEmail(String email);
}
