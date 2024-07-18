package com.example.mockproject.repository;


import com.example.mockproject.dto.response.CandidateViewResponse;
import com.example.mockproject.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Optional<Candidate> findCandidateByPersonEmail(String email);


    @Query(value = "SELECT c FROM Candidate c LEFT JOIN Person p ON c.person= p.candidate WHERE p.fullName = ?1")
    Candidate findByCandidateName(String name);

    Optional<Candidate> findByPersonPersonId(Integer personId);


    @Query(value = "SELECT new com.example.mockproject.dto.response.CandidateViewResponse( c.candidateId, c.recruiter.account,c.candidateStatus,p.email,p.fullName,p.phoneNumber,c.positionEnum  )" +
            " FROM  Candidate c INNER JOIN  c.person p " +
            "ON c.person.personId=p.personId WHERE ((p.fullName LIKE %?1% OR p.email LIKE %?1% OR p.phoneNumber LIKE %?1% OR c.recruiter.account LIKE %?1% )AND ( ?2 IS NULL OR c.candidateStatus = ?2))")
    Page<CandidateViewResponse> findCandidate(String key, Integer status, Pageable pageable);

    @Query(value = " SELECT  c.candidate_id, p.full_name " +
            " FROM candidate c , person p \n" +
            " WHERE c.person_id = p.person_id \n" +
            " AND c.status = :status", nativeQuery = true)
    List<Object> getCandidateListByStatus(Integer status);

}
