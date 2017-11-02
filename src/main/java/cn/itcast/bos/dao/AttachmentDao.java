package cn.itcast.bos.dao;


import cn.itcast.bos.domain.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by gys on 2017/4/13.
 */
@Mapper
public interface AttachmentDao extends BaseDAO<Attachment> {

    List<Attachment> findByForeignKey(@Param("key") String key);

    void updateForeignKey(@Param("id") String id, @Param("key") String key);
}
