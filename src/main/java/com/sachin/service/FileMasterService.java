package com.sachin.service;

import com.sachin.dao.FileMasterDao;
import com.sachin.entity.FileMaster;
import com.sachin.entity.UserDetailsAndSkills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FileMasterService {
    @Autowired
    private FileMasterDao fileMasterDao;


    public FileMaster addFileMaster(String fileName,String userDeatilsEmail)
    {
        FileMaster fileMaster=new FileMaster();
        fileMaster.setFileDirectory("E:\\SpringBoot\\Project5\\src\\main\\resources\\static\\images\\"+fileName);
        fileMaster.setCreated_at(LocalDateTime.now());
        fileMaster.setUpdated_at(null);
        fileMaster.setDeleted_at(null);
        fileMaster.setUserDetailsId(userDeatilsEmail);
        return fileMaster;
    }

    public FileMaster addFileMaster(FileMaster fileMaster)
    {
        return fileMasterDao.save(fileMaster);
    }

    public void deleteFileMasterById(int id)
    {
        fileMasterDao.deleteById(id);
    }

    public FileMaster getFileMasterById(int id)
    {
        return fileMasterDao.findById(id).get();
    }

    public List<FileMaster> getAllFileMaster()
    {
        return (List<FileMaster>)fileMasterDao.findAll();
    }

    public void deleteByUserDetailsId(String id)
    {
        fileMasterDao.deleteByUserDetailsId(id);
    }

    @Modifying
    public void updateFileMaster(String id,FileMaster newFileMaster)
    {
        fileMasterDao.deleteByUserDetailsId(id);
        FileMaster fm=fileMasterDao.save(newFileMaster);
    }
}
