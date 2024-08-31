package com.hello.service.post.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ArticleBloomFilterService {

    private final BloomFilter<Integer> bloomFilter;

    public ArticleBloomFilterService(
            @Value("${bloomfilter.expectedInsertions}") int expectedInsertions,
            @Value("${bloomfilter.falsePositiveProbability}") double falsePositiveProbability) {
        this.bloomFilter = BloomFilter.create(Funnels.integerFunnel(), expectedInsertions, falsePositiveProbability);
    }

    /**
     * 根据文章id和用户id实现去重
     * @param userId
     * @param articleId
     * @return
     */
    public boolean isArticleRead(Long userId, Long articleId) {
        int uniqueId = Objects.hash(userId, articleId);

        return bloomFilter.mightContain(uniqueId);
    }

    public void markArticleAsRead(Long userId, Long articleId) {
        int uniqueId = Objects.hash(userId, articleId);
        bloomFilter.put(uniqueId);
    }
}
