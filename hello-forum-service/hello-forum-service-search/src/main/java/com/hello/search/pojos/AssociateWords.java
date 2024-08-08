package com.hello.search.pojos;

import com.hello.common.constants.SearchPostConstants;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 联想词表
 * </p>
 *
 * @author itheima
 */
@Data
@Document(SearchPostConstants.USER_SEARCH_ASSOCIATE_WORDS_TABLE)
public class AssociateWords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    /**
     * 联想词
     */
    private String associateWords;

    /**
     * 创建时间
     */
    private Date createdTime;

}