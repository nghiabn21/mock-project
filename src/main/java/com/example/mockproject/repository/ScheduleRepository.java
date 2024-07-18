package com.example.mockproject.repository;


import com.example.mockproject.model.Schedule;
import com.example.mockproject.utils.enums.ScheduleStatusEnum;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = " select distinct s.schedule_id , s.schedule_title, s.\"location\" , s.meeting_id , s.notes , s.\"result\" , " +
            "s.status, s.schedule_from , s.schedule_to , s.candidate_id , s.recruiter_owner_id, s.created_by , s.created_date , " +
            "s.last_modified_by , s.last_modified_date " +
            "from schedule s, schedule_interviewers si \n" +
            "where s.schedule_id = si.schedule_id " +
            "and s.schedule_title ilike %:title% \n" +
            "and ( COALESCE( null, :interviewerIds ) is null or (si.interviewer_id in :interviewerIds )) " +
            "and ( COALESCE( null, :statusList ) is null or (s.status in :statusList) )  ", nativeQuery = true)
    Page<Schedule> findAllByTitleAndInterviewerAndStatus(String title,
                                                         List<Integer> interviewerIds,
                                                         List<Integer> statusList,
                                                         Pageable pageable);

    @Query(value = "select count( s.schedule_id)\n" +
            "from schedule s\n" +
            "where  s.candidate_id  = :candidateId \n" +
            "and ( (s.schedule_from >= :scheduleFrom and s.schedule_from < :scheduleTo ) \n" +
            "or (s.schedule_to > :scheduleFrom and s.schedule_to  <= :scheduleTo )\n" +
            "or (s.schedule_from < :scheduleFrom and s.schedule_to > :scheduleTo ) )", nativeQuery = true)
    Integer countOverlapSchedule(Integer candidateId, Date scheduleFrom, Date scheduleTo);

    @Query(value = "select count(s.schedule_id) \n" +
            "from schedule s, schedule_interviewers si \n" +
            "where s.schedule_id = si.schedule_id " +
            "and si.interviewer_id = :interviewerId " +
            "and s.status = :status ", nativeQuery = true)
    Integer countCurUserScheByStatus(Integer interviewerId, Integer status);

    @Modifying
    @Query(value = " UPDATE schedule  SET status = :status, result = :result WHERE schedule_id = :scheduleId ", nativeQuery = true)
    void updateStatusSchedule(Integer status, Integer result, Integer scheduleId);

    @Modifying
    @Query(value = " UPDATE Schedule SET scheduleStatus = :inProgressStatus " +
            "WHERE scheduleFrom >= current_date " +
            "AND scheduleTo < current_date +1 " +
            "AND scheduleStatus = 1")
    void updateInProgressSchedule(ScheduleStatusEnum inProgressStatus);

    @Modifying
    @Query(value = " UPDATE Schedule SET scheduleStatus = :cancelStatus WHERE scheduleTo < current_date AND scheduleStatus = 2")
    void updateOverDateSchedule(ScheduleStatusEnum cancelStatus);


    @Query(value = "select  s.schedule_id , s.schedule_title, s.location , s.meeting_id , s.notes , s.result, " +
        "s.status, s.schedule_from , s.schedule_to , s.candidate_id , s.recruiter_owner_id, s.created_by , s.created_date , " +
        "s.last_modified_by , s.last_modified_date"
        + " from Person p, Schedule s, Candidate c, User u "
        + "where c.person_id = p.person_id and\n"
        + "s.candidate_id = c.candidate_id and DATE(s.schedule_from) = :date", nativeQuery = true)
    List<Schedule> findAllEmailInterview(LocalDate date);

    Optional<Schedule> findByScheduleIdAndScheduleStatus(int id, ScheduleStatusEnum status);

    @Query(value = "select s.schedule_id, s.schedule_title, p.full_name " +
            "from schedule s, candidate c, person p " +
            "where s.candidate_id = c.candidate_id " +
            "and c.person_id = p.person_id " +
            "and s.status = :status " +
            "and s.schedule_id not in (select o.schedule_id from offer o) ", nativeQuery = true)
    List<Object> getScheduleListByStatus(Integer status);
}
