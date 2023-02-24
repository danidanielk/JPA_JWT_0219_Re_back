package com.kim.dani.repository;

import com.kim.dani.entity.Department;
import com.kim.dani.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Department,Long> {

}


