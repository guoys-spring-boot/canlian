package cn.itcast.bos.dao.business;

import cn.itcast.bos.dao.BaseDAO;
import cn.itcast.bos.domain.business.Person;
import cn.itcast.bos.domain.business.PlayMoneyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by gys on 2017/8/26.
 */

@Mapper
public interface PlayMoneyDao extends BaseDAO<PlayMoneyRecord> {
    void batchInsert(@Param("list") List<PlayMoneyRecord> list);
}
