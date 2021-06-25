package com.sachin.service;

import com.sachin.dao.UserDetailsDao;
import com.sachin.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsService {

    @Autowired
    private UserDetailsDao userDetailsDao;

    public UserDetails addUserDetails(UserDetails userDetails)
    {
        return userDetailsDao.save(userDetails);
    }

    public UserDetails getUserDetailsByEmail(String email)
    {
        return userDetailsDao.getUserDetailsByEmail(email);
    }

    @Modifying
    public void updateUserDetails(String id,UserDetails newUserDetails)
    {
        userDetailsDao.deleteById(id);
        UserDetails ud=userDetailsDao.save(newUserDetails);
    }

}
