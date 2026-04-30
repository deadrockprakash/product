package com.prakash.productservice.repository;

import com.prakash.productservice.dto.UserDto;
import com.prakash.productservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByUserName(String userName);
}
