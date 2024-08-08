package com.hello.search.pojos;

import com.hello.common.constants.SearchPostConstants;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document(SearchPostConstants.USER_SEARCH_RECORDS_TABLE)
@Builder
public class UserSearchRecord {
    private static final Integer serialVersionUID = 1;

    /**
     * 主键
     */
    @Id
    private ObjectId id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 搜索词
     */
    private String keyword;

    /**
     * 创建时间
     */
    private Date createdTime;
}
