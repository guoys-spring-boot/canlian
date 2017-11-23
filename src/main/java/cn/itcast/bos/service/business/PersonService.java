package cn.itcast.bos.service.business;

import cn.itcast.bos.domain.business.Person;

import java.io.IOException;
import java.util.List;

/**
 * Created by gys on 2017/8/26.
 */
public interface PersonService {

    /**
     * 合并数据， 合并所有重复的数据， 以最新上传的数据为主
     * 身份证，残疾证和银行卡号三个相同，就视为同一条数据
     * 返回合并的条数
     */
    long compact();

    Person getById(String id);

    void update(Person person);

    void insert(Person person);

    List<Person> listAllPerson(Person person);

    List<Person> listPerson(Person person);

    List<String> listAllArea();

    void importExcel(byte[] excel, String uploadDate) throws IOException;

    List<String> getLackBankId(byte[] excel) throws IOException;

    void delete(String personId);
}
