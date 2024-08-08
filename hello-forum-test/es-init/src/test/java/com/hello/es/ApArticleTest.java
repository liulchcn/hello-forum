package com.hello.es;

import com.alibaba.fastjson.JSON;
import com.hello.common.constants.SearchPostConstants;
import com.hello.es.mapper.PostMapper;
import com.hello.es.pojo.SearchPostVO;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ApArticleTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 注意：数据量的导入，如果数据量过大，需要分页导入
     * @throws Exception
     */
    @Test
    public void init() throws Exception {

        //1.查询所有符合条件的文章数据
        List<SearchPostVO> searchPostVOS = postMapper.loadPostList();

        //2.批量导入到es索引库

        BulkRequest bulkRequest = new BulkRequest(SearchPostConstants.GLOBAL_INDEX_NAME);

        for (SearchPostVO searchPostVO : searchPostVOS) {

            IndexRequest indexRequest = new IndexRequest().id(searchPostVO.getId().toString())
                    .source(JSON.toJSONString(searchPostVO), XContentType.JSON);

            //批量添加数据
            bulkRequest.add(indexRequest);

        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    }

}