package com.hello.search.listener;

import com.alibaba.fastjson.JSON;
import com.hello.common.constants.SearchPostConstants;
import com.hello.common.constants.StudioPostMessageConstants;
import com.hello.model.search.vos.SearchPostAsInfoVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncPostListener {

    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics = SearchPostConstants.POST_ES_SYNC_TOPIC)
    public void onMessage(String message){
        if(StringUtils.isNotBlank(message)){
            log.info("SyncPostListener,message={}",message);

            SearchPostAsInfoVO searchPostAsInfoVO = JSON.parseObject(message, SearchPostAsInfoVO.class);
            IndexRequest request = new IndexRequest(SearchPostConstants.GLOBAL_INDEX_NAME);
            request.id(searchPostAsInfoVO.getPostId().toString());
            request.source(message, XContentType.JSON);
            try{
                restHighLevelClient.index(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Sync es error :{}",e);

            }
        }
    }
}
