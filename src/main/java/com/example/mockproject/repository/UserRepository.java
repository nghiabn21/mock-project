package com.example.mockproject.repository;

import com.example.mockproject.model.User;
import com.example.mockproject.utils.enums.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByAccount(String account);

    boolean existsByAccountIgnoreCaseAndStatus(String account, UserStatusEnum userStatusEnum);

    Optional<User> findByAccount(String account);

    User findUserByAccountIgnoreCase(String account);

    User findByResetPasswordToken(String token) ;

    User findUserByPersonEmail(String email);

    Page<User> findAllByAccountAndPerson_Email(Pageable pageable, String account , String email);

    @Query(value = " select distinct u.user_id, u.account, p.phone_number,u.status," +
            " u.created_by, u.created_date, u.last_modified_by, u.last_modified_date," +
            " u.department, u.expiry_date, u.notes, u.password, u.reset_password_token, " +
            " u.temporary_password, u.person_id " +
            " from users u, person p, user_roles ur " +
            " where u.person_id = p.person_id " +
            " and u.user_id = ur.user_id " +
            " and (:account is null or u.account ilike %:account%) " +
            " and (:role is null or ur.role_id = :role) "
            , nativeQuery = true)
    Page<User> findAllByAccountAndRoles(Pageable pageable, String account, Integer role);

    Page<User> findAll(Pageable pageable);

    @Query(value = "select account " +
            "from users u " +
            "join user_roles ur " +
            "on u.user_id = ur.user_id " +
            "where u.status = 1 " +
            "and ur.role_id = 2", nativeQuery = true)
    List<String> findAllAccount();

    @Query(value = "SELECT u.user_id , u.account " +
            " FROM users u , user_roles ur \n" +
            " WHERE u.user_id = ur.user_id \n " +
            " AND u.status = :status " +
            " AND ur.role_id = :roleId ", nativeQuery = true)
    List<Object> getUserListByStatusAndRole(Integer status, Integer roleId);

    @Query("SELECT a.account FROM User a WHERE a.account LIKE :account%")
    List<String> findByUsernameEndWith(@Param("account") String account);
}
