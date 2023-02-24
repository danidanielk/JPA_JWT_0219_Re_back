package com.kim.dani.service;

import com.kim.dani.repository.LikeRepository;
import com.kim.dani.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;


}
