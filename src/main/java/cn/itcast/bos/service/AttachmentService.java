package cn.itcast.bos.service;



import cn.itcast.bos.domain.Attachment;

import java.util.List;

/**
 * Created by gys on 2017/4/13.
 */

public interface AttachmentService {

    void insert(Attachment attachment);

    List<Attachment> findByForeignKey(String key);

    void updateForeign(String id, String key);

    Attachment findById(String id);

    void deleteById(String id);
}
