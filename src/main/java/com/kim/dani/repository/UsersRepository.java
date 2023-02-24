package com.kim.dani.repository;

import com.kim.dani.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long>  {
    public Users findByemail (String email);


}
