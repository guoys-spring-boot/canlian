package cn.itcast.bos.dao.business;

import cn.itcast.bos.dao.BaseDAO;
import cn.itcast.bos.domain.business.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by gys on 2017/8/26.
 */

@Mapper
public interface PersonDao extends BaseDAO<Person> {
    List<Person> listAllAreas();

    int countPersonByIdentity(@Param("identity") String identity);
}
