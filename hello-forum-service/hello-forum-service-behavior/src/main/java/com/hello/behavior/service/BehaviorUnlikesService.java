package com.hello.behavior.service;

import com.hello.model.behavior.dtos.BehaviorLikesPostDTO;
import com.hello.model.behavior.dtos.BehaviorReadDTO;
import com.hello.model.behavior.dtos.BehaviorUnlikesPostDTO;

public interface BehaviorUnlikesService {


    void unlike(BehaviorUnlikesPostDTO behaviorLikesPostDTO);

}
