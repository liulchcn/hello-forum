package com.hello.model.search.vos;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class PostSearchVO {
    List<Map> list;
}
