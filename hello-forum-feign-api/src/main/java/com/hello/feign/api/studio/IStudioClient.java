package com.hello.feign.api.studio;

import com.hello.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("forum-studio")
public interface IStudioClient {
    @GetMapping("/api/v1/channel/list")
    public Result getChannels();
}
