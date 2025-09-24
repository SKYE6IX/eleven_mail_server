package com.eleven.mail_server.auth.repository;

import com.eleven.mail_server.auth.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("SELECT \"username\", \"harsh_password\" FROM \"USER\" u WHERE u.username = :username")
    Optional<UserEntity> findByUserName(@Param("username") String username);

}
