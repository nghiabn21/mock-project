package com.example.mockproject.repository;

import com.example.mockproject.dto.response.OfferViewDto;
import com.example.mockproject.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    @Query(value = "select o.offer_id, o.position, o.offer_status, o.contract_start, o.contract_end, o.note, o.contract_type," +
            "o.level, o.department, o.recruiter_owner_id, o.due_date, o.basic_salary,  " +
            "o.schedule_id, o.manager_id, o.created_by, o.created_date, o.last_modified_by, o.last_modified_date " +
            "from Offer o " +
            "join schedule s on o.schedule_id = s.schedule_id " +
            "join candidate c on s.candidate_id = c.candidate_id " +
            "join person p on p.person_id = c.person_id " +
            "where ( :name is null or p.full_name ilike %:name% or p.email ilike %:name% )" +
            "and ( :department is null or o.department = :department ) " +
            "and ( :status is null or o.offer_status = :status ) ", nativeQuery = true)
    Page<Offer> findAllByCandidateNameAndDepartmentAndStatus(String name, Integer department, Integer status, Pageable pageable);


    @Query(value = "select * from offer o where o.schedule_id = :scheduleId ", nativeQuery = true)
    Optional<Offer> findByScheduleId(Integer scheduleId);


    @Query(value = " select p.full_name,u.account,p.email, " +
            " o.created_by, o.created_date, o.last_modified_by, o.last_modified_date,o.basic_salary, " +
            " o.offer_id, o.position, o.offer_status, o.contract_start, o.contract_end, o.note, o.contract_type, " +
            " o.level, o.department, o.recruiter_owner_id, o.due_date, o.basic_salary, " +
            " o.schedule_id, o.manager_id, o.created_by, o.created_date, o.last_modified_by, o.last_modified_date " +
            " from Offer o, Schedule s, Candidate c, Person p, Users u " +
            " where  o.schedule_id = s.schedule_id and s.candidate_id = c.candidate_id " +
            " and c.person_id = p.person_id and p.person_id = u.person_id  " +
            " and DATE ( o.due_date ) = :date " +
            " and o.offer_status = 0 ", nativeQuery = true)
    List<Offer> findAllByDueDate(LocalDate date);

    @Query(value = " select o.offer_id, o.position, o.contract_start, o.contract_end, " +
            " o.note, o.contract_type,o.level, o.department," +
            " o.recruiter_owner_id, o.due_date, o.basic_salary," +
            " s.candidate_id, o.manager_id, o.created_by, o.created_date," +
            " o.last_modified_by, o.last_modified_date ,u.account," +
            " o.offer_status, o.position, o.manager_id, o.recruiter_owner_id, o.schedule_id " +
            " from Offer o " +
            " join  schedule s on o.schedule_id = s.schedule_id " +
            " join users u on u.user_id = o.manager_id " +
            " where o.offer_id = :id "
            , nativeQuery = true)
    Optional<Offer> findByOfferId(int id);

    @Query(value = "select o from Offer o where o.contractStart >= :dateFrom and o.contractStart <= :dateTo")
    List<Offer> findByContractStart(Date dateFrom, Date dateTo);
    @Query(value = "select count(o.offer_id) from offer o " +
            "join users u on o.manager_id = u.user_id " +
            "where o.manager_id = :managerId " +
            "and u.status = 1 " +
            "and o.offer_status = 0 ",
             nativeQuery = true)
    Integer countCurOfferByManagerIdAndOfferStatus(int managerId);
    @Query(value = "select o from Offer o " +
            "where o.manager.userId = :managerId " +
            "and o.offerStatusEnum = 0 ")
    List<Offer> findByManagerIdAndOfferStatus(Integer managerId);

}
