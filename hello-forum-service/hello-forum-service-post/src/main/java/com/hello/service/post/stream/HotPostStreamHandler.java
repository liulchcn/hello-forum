package com.hello.service.post.stream;

import com.alibaba.fastjson.JSON;
import com.hello.common.constants.HotPostConstants;
import com.hello.model.mess.PostVisitStreamMess;
import com.hello.model.mess.UpdatePostMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class HotPostStreamHandler {
    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        KStream<String,String> stream = streamsBuilder.stream(HotPostConstants.HOT_POST_SCORE_TOPIC);

        stream.map((key,value) -> {
            UpdatePostMess updatePostMess = JSON.parseObject(value, UpdatePostMess.class);
            return new KeyValue<>(updatePostMess.getPostId().toString(),updatePostMess.getType().name()+":"+updatePostMess.getAdd());
        })
                .groupBy((key,value) -> key)
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                .aggregate(new Initializer<String>() {
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                 }, new Aggregator<String, String, String>() {
                    @Override
                    public String apply(String key, String value, String aggValue) {
                    System.out.println(value);
                    if(StringUtils.isBlank(value)){
                        return aggValue;
                    }
                    String[] aggAry = aggValue.split(",");
                    int col = 0,com=0,lik=0,vie=0;
                    for (String agg : aggAry) {
                        String[] split = agg.split(":");
                        /**
                         * 获得初始值，也是时间窗口内计算之后的值
                         */
                        switch (UpdatePostMess.UpdatePostType.valueOf(split[0])){
                            case COLLECTION:
                                col = Integer.parseInt(split[1]);
                                break;
                            case COMMENT:
                                com = Integer.parseInt(split[1]);
                                break;
                            case LIKES:
                                lik = Integer.parseInt(split[1]);
                                break;
                            case VIEWS:
                                vie = Integer.parseInt(split[1]);
                                break;
                        }
                    }
                    /**
                     * 累加操作   likes:1
                     */
                    String[] valAry = value.split(":");
                    switch (UpdatePostMess.UpdatePostType.valueOf(valAry[0])){
                        case COLLECTION:
                            col += Integer.parseInt(valAry[1]);
                            break;
                        case COMMENT:
                            com += Integer.parseInt(valAry[1]);
                            break;
                        case LIKES:
                            lik += Integer.parseInt(valAry[1]);
                            break;
                        case VIEWS:
                            vie += Integer.parseInt(valAry[1]);
                            break;
                    }

                    String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
                    System.out.println("帖子的id:"+key);
                    System.out.println("当前时间窗口内的消息处理结果："+formatStr);
                    return formatStr;
                }
                }, Materialized.as("hot-post-stream-count-001"))
                .toStream()
                .map((key,value)->{
                    return new KeyValue<>(key.key().toString(),formatObj(key.key().toString(),value));
                })
                //发送消息
                .to(HotPostConstants.HOT_POST_INCR_HANDLE_TOPIC);
        return stream;
    }
    public String formatObj(String postId,String value){
        PostVisitStreamMess mess = new PostVisitStreamMess();
        mess.setPostId(Long.valueOf(postId));
        //COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdatePostMess.UpdatePostType.valueOf(split[0])){
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        log.info("聚合消息处理之后的结果为:{}",JSON.toJSONString(mess));
        return JSON.toJSONString(mess);

    }
}
