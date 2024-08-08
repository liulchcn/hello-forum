package com.hello.search.service;

public interface UserSearchService {
    /**
     * 保存历史记录
     * @param keyword 关键词
     * @param userId 用户ID
     */
    public void insert(String keyword,Integer userId);

}
