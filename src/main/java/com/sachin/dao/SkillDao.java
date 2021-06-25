package com.sachin.dao;

import com.sachin.entity.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillDao extends CrudRepository<Skill,Integer> {
    public List<Skill> getAllByUserDetailsId(String userDetailsId);

    @Query(value = "select skill.name from skill where user_details_id = ?1",nativeQuery = true)
    public List<String> getAllNameByUserDetailsId(String userDetailsId);

    public void deleteByUserDetailsId(String id);


}
