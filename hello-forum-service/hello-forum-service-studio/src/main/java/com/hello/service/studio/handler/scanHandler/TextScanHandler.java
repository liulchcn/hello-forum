package com.hello.service.studio.handler.scanHandler;

import com.hello.model.studio.constants.StudioPostConstants;
import com.hello.service.studio.handler.AuitHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.hello.model.studio.constants.StudioPostConstants.STUDIO_POST_CONTENT;

@Component
public class TextScanHandler implements AuitHandler<Map<String, Object>> {
    private AuitHandler<Map<String, Object>> nextHandler;


    @Override
    public void setNext(AuitHandler<Map<String, Object>> nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(Map<String, Object> context) {
        if(context.get(STUDIO_POST_CONTENT)!=null){
            System.out.println("审核文章");
        }
        if (nextHandler != null){
            nextHandler.handle(context);
        }
    }
}
