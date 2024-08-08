package com.hello.service.studio.feign;

import com.hello.common.result.Result;
import com.hello.feign.api.studio.IStudioClient;
import com.hello.model.studio.pojos.StudioChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudioClient implements IStudioClient {

    @GetMapping("/api/v1/channel/list")
    @Override
    public Result getChannels() {
        return null;
    }
}
