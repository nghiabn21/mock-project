package com.example.mockproject.repository;

import com.example.mockproject.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    Job findByJobTitleIgnoreCase(String jobTitle);
    List<Job> findAll();

    @Query(value = "select " +
            "        j.job_id, " +
            "        j.job_title, " +
            "        j.start_date, " +
            "        j.end_date, " +
            "        j.salary_from, " +
            "        j.salary_to, " +
            "        j.working_address, " +
            "        j.description, " +
            "        j.status , " +
            "   j.created_by , " +
            "        j.created_date, " +
            "        j.last_modified_by, " +
            "        j.last_modified_date  " +
            "           from job j " +
            "           where j.job_title like %?1% " +
            "            and j.status = ?2  " +
            "        "
            , nativeQuery = true)
    Page<Job> findByStartDateAndJobTitleAndJobStatus(String job_title, Integer status, Pageable pageable);


    @Query(value = "from Job j where (j.jobTitle like %?1%) and  (?2 is null or j.jobStatus = ?2)")
    Page<Job> findByJobTitleAndAndJobStatus(String job_title, Integer jobStatusEnum, Pageable pageable);


    @Query(value = "select j from Job j join j.skills s join j.levels l " +
            "where (j.jobTitle like %?1%) " +
            "and  (?2 is null or j.jobStatus = ?2) " +
            "and ((?3 is null or s.name like %?3% ) or (?3 is null or l.name like %?3%))")
    Page<Job> findByJobListAndAndSkillsAndAndLevels(String job_title, Integer jobStatusEnum, String key, Pageable pageable);



}
