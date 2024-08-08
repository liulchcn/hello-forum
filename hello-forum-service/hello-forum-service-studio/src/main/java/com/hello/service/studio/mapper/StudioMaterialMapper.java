package com.hello.service.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.studio.pojos.StudioMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudioMaterialMapper extends BaseMapper<StudioMaterial> {
    void saveRelations(@Param("materialIds") List<Integer> idList, @Param("postId") Integer id, @Param("type") Integer type);

}
