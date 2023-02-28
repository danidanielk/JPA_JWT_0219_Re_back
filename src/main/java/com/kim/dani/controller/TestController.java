package com.kim.dani.controller;


import com.kim.dani.dto.TestDto;
import com.kim.dani.dto.TestSaveDto;
import com.kim.dani.entity.Department;
import com.kim.dani.entity.Member;
import com.kim.dani.repository.DepartmentRepository;
import com.kim.dani.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {



    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;



    @PostMapping("/save")
    public void save(@RequestBody TestSaveDto testSaveDto){
        Member member = new Member();
        Department department = new Department();

        department.setName(testSaveDto.getDepartmentName());
        departmentRepository.save(department);
        member.setName(testSaveDto.getMemberName());
        member.setAge(testSaveDto.getMemberAge());
        member.setDepartment(department);
        memberRepository.save(member);
    }

    @GetMapping("/select")
    public ResponseEntity testDto (){
        String departmentName = "good";
        List<Member> members =memberRepository.findAll();
        List<TestDto> testDtos = new ArrayList<>();
        for (Member member : members) {
            if(member.getDepartment().getName().equals(departmentName)){
                TestDto testDto= new TestDto();
                testDto.setAge(member.getAge());
                testDto.setName(member.getName());
                testDtos.add(testDto);
            }
        }
        return new ResponseEntity(testDtos, HttpStatus.OK);
    }


}
