package com.hello.search.vos;

import com.hello.common.constants.SearchPostConstants;
import com.hello.search.pojos.UserSearchRecord;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(SearchPostConstants.USER_SEARCH_RECORDS_TABLE)
@Builder
public class UserSearchRecordsVO {
    List<UserSearchRecord> resultList;
}
