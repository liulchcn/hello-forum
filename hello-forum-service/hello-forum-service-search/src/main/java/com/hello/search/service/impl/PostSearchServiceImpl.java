package com.hello.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hello.common.constants.SearchPostConstants;
import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.exception.SearchException;
import com.hello.search.pojos.AssociateWords;
import com.hello.search.pojos.UserSearchRecord;
import com.hello.search.vos.HistorySearchDTO;
import com.hello.search.vos.UserSearchRecordsVO;
import com.hello.model.search.dtos.UserSearchDTO;
import com.hello.model.search.vos.PostSearchVO;
import com.hello.search.service.PostSearchService;
import com.hello.search.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private UserSearchService userSearchService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public PostSearchVO search(UserSearchDTO userSearchDTO) throws IOException {
        if(userSearchDTO == null || StringUtils.isBlank(userSearchDTO.getSearchWords())){
            throw new SearchException(SearchPostConstants.PARAM_INVALID);
        }

        Integer currentUserId = UserThreadLocalUtil.getCurrentId();
        log.info("获取当前用户的用户id：{}",currentUserId);
        if(currentUserId!=null){
            userSearchService.insert(userSearchDTO.getSearchWords(),currentUserId);
        }

        SearchRequest searchRequest = new SearchRequest(SearchPostConstants.GLOBAL_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /**
         * 设置布尔查询
         */
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //关键词查询
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(userSearchDTO.getSearchWords()).field(SearchPostConstants.SEARCH_FIELD_TITLE)
                .field(SearchPostConstants.SEARCH_FIELD_CONTENT).defaultOperator(Operator.OR);
        boolQueryBuilder.must(queryStringQueryBuilder);

        //设置截至日期查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(SearchPostConstants.SEARCH_FIELD_PUBLISH_TIME)
                .lt(userSearchDTO.getMinPublishTime().getTime());
        boolQueryBuilder.filter(rangeQueryBuilder);
        /**
         *bool查询设置完毕
         */
        /**
         * 分页查询
         */
        searchSourceBuilder.from(SearchPostConstants.INIT_PAGE);
        /**
         * TODO 暂时实验中缺少发布时间
         */
        //searchSourceBuilder.sort(SearchPostConstants.SEARCH_FIELD_PUBLISH_TIME, SortOrder.DESC);


        /**
         * 设置高亮
         */
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(SearchPostConstants.SEARCH_FIELD_TITLE);
        highlightBuilder.preTags(SearchPostConstants.SEARCH_HIGHLIGHT_STYLE_PRE_TAGS);
        highlightBuilder.postTags(SearchPostConstants.SEARCH_HIGHLIGHT_STYLE_POST_TAGS);
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        /**
         * 返回结果
         */
        ArrayList<Map> list = new ArrayList<>();
        SearchHit[] hits = searchResponse.getHits().getHits();

        for (SearchHit hit : hits) {
            String hitSourceAsString = hit.getSourceAsString();
            Map map = JSON.parseObject(hitSourceAsString, Map.class);
            /**
             * 这个逻辑确保了只有在高亮字段存在时才会用高亮的内容，否则使用原始标题。
             * 这种方式避免了重复并且确保了高亮显示的效果。
             */
            if(hit.getHighlightFields()!=null && hit.getHighlightFields().size()>0){
                Text[] fragments = hit.getHighlightFields().get(SearchPostConstants.SEARCH_FIELD_TITLE).getFragments();
                String title = StringUtils.join(fragments);
                map.put(SearchPostConstants.RESULT_TITLE,title);
            }else{
                map.put(SearchPostConstants.RESULT_TITLE,map.get(SearchPostConstants.SEARCH_FIELD_TITLE));
            }
            list.add(map);
        }
        PostSearchVO postSearchVO = PostSearchVO.builder()
                .list(list)
                .build();
        return postSearchVO;
    }

    @Override
    public UserSearchRecordsVO findSearchRecords() {
        Integer currentId = UserThreadLocalUtil.getCurrentId();
        if(currentId==null){
            throw new SearchException(SearchPostConstants.PARAM_INVALID) ;
        }
        List<UserSearchRecord> userSearchRecordsVOS = mongoTemplate.find(Query.query(Criteria.where(SearchPostConstants.USER_SEARCH_USER_ID).is(currentId))
                .with(Sort.by(Sort.Direction.DESC, SearchPostConstants.USER_SEARCH_SORT_BY_CREATE_TIME)), UserSearchRecord.class);
        UserSearchRecordsVO userSearchRecordsVO = UserSearchRecordsVO.builder()
                .resultList(userSearchRecordsVOS).build();
        return userSearchRecordsVO;
    }

    @Override
    public void delete(HistorySearchDTO dto) {
        if(dto.getId() == null){
            throw new SearchException(SearchPostConstants.PARAM_INVALID);
        }
        Integer currentId = UserThreadLocalUtil.getCurrentId();

        if(currentId == null){
            throw new SearchException(SearchPostConstants.PARAM_INVALID);
        }

        mongoTemplate.remove(Query.query(Criteria.where(SearchPostConstants.USER_SEARCH_USER_ID).is(currentId)
                .and(SearchPostConstants.USER_SEARCH_ID).is(dto.getId()))
                ,UserSearchRecord.class);

    }

    @Override
    public List<AssociateWords> findAssociate(UserSearchDTO userSearchDto) {
        if(userSearchDto == null || StringUtils.isBlank(userSearchDto.getSearchWords())){
            throw new SearchException(SearchPostConstants.PARAM_INVALID);
        }
        if(userSearchDto.getPageSize() > 10){
            userSearchDto.setPageSize(10);
        }
        Query query = Query.query(Criteria.where(SearchPostConstants.ASSOCIATE_QUERY_BY_ASSOCIATE_WORDS)
                .regex(".*?\\" + userSearchDto.getSearchWords() + ".*"));
        query.limit(userSearchDto.getPageSize());
        List<AssociateWords> apAssociateWords = mongoTemplate.find(query, AssociateWords.class);
        return apAssociateWords;
    }
}
