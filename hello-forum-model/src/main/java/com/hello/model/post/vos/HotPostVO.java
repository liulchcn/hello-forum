package com.hello.model.post.vos;

import com.hello.model.post.pojos.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class HotPostVO extends Post {

    private Integer score;
}
