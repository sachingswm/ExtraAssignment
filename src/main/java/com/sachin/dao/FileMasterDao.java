package com.sachin.dao;

import com.sachin.entity.FileMaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMasterDao extends CrudRepository<FileMaster,Integer> {
    public void deleteByUserDetailsId(String id);
}
