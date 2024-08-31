package com.hello.service.studio.mapper;

import cn.hutool.core.lang.ObjectId;
import com.hello.model.studio.pojos.StudioVideo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudioVideoRepository extends MongoRepository<StudioVideo, ObjectId> {
}
