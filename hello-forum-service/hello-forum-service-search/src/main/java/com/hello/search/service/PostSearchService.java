package com.hello.search.service;


import com.hello.search.pojos.AssociateWords;
import com.hello.search.vos.HistorySearchDTO;
import com.hello.search.vos.UserSearchRecordsVO;
import com.hello.model.search.dtos.UserSearchDTO;
import com.hello.model.search.vos.PostSearchVO;

import java.io.IOException;
import java.util.List;

public interface PostSearchService {
    PostSearchVO search(UserSearchDTO userSearchDTO) throws IOException;

    UserSearchRecordsVO findSearchRecords();

    void delete(HistorySearchDTO historySearchDTO);

    List<AssociateWords> findAssociate(UserSearchDTO userSearchDto);
}
