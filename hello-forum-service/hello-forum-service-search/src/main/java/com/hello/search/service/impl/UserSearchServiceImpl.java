package com.hello.search.service.impl;

import com.hello.common.constants.SearchPostConstants;
import com.hello.search.pojos.UserSearchRecord;
import com.hello.search.service.UserSearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserSearchServiceImpl implements UserSearchService {
    private MongoTemplate mongoTemplate;
    @Autowired
    public UserSearchServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        if (mongoTemplate == null) {
            System.out.println("MongoTemplate 注入失败");
        } else {
            System.out.println("MongoTemplate 注入成功");
        }
    }
    @Override
    public void insert(String keyword, Integer userId) {
        log.info("存储查找历史记录：{}",keyword);
        Query query = Query.query(Criteria.where(SearchPostConstants.USER_SEARCH_KEY_WORD).is(keyword));
        UserSearchRecord userSearchRecord = mongoTemplate.findOne(query, UserSearchRecord.class);
        if(userSearchRecord !=null){
            userSearchRecord.setCreatedTime(new Date());
            mongoTemplate.save(userSearchRecord);
            return;
        }

        userSearchRecord = UserSearchRecord.builder()
                .userId(userId)
                .keyword(keyword)
                .createdTime(new Date())
                .build();
        Query query1 = Query.query(Criteria.where(SearchPostConstants.USER_SEARCH_USER_ID).is(userId));
        query1.with(Sort.by(Sort.Direction.DESC,SearchPostConstants.USER_SEARCH_SORT_BY_CREATE_TIME));
        List<UserSearchRecord> userSearchRecordsList = mongoTemplate.find(query1, UserSearchRecord.class);
        if(userSearchRecordsList==null || userSearchRecordsList.size()<10){
            mongoTemplate.save(userSearchRecord);
        }else {
            UserSearchRecord expiredRecord = userSearchRecordsList.get(userSearchRecordsList.size() - 1);
            mongoTemplate.findAndReplace(Query.query(Criteria.where(SearchPostConstants.USER_SEARCH_ID).is(expiredRecord.getId())),userSearchRecord);
        }
    }
}
