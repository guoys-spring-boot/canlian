package cn.itcast.bos.service.business.impl;

import cn.itcast.bos.dao.business.PersonDao;
import cn.itcast.bos.domain.business.Person;
import cn.itcast.bos.service.business.PersonService;
import cn.itcast.bos.utils.POIHelper;
import cn.itcast.bos.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gys on 2017/8/26.
 */

@Service
@Transactional
public class PersonServiceImp implements PersonService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonDao dao;

    @Override
    public List<Person> listAllPerson(Person person) {
        return dao.findByCondition(person);
    }

    @Override
    public List<Person> listPerson(Person person) {
        return null;
    }

    @Override
    public void insert(Person person) {
        person.setId(UUIDUtils.generatePrimaryKey());
        dao.insert(person);
    }

    @Override
    public Person getById(String id) {
        return dao.findById(id);
    }

    @Override
    public void update(Person person) {
        dao.update(person);
    }

    @Override
    public void importExcel(byte[] excel, String uploadDate) throws IOException{

        List<Person> needInsert = needInsertFromExcel(excel);
        for (Person person : needInsert) {
            person.setId(UUIDUtils.generatePrimaryKey());
            person.setZhuangtai("3");
            person.setUploadDate(uploadDate);
            dao.insert(person);
        }

        List<Person> needUpdate = needUpdateFromExcel(excel);
        for (Person person : needUpdate) {
            dao.update(person);
        }

    }

    @Override
    public List<String> getLackBankId(byte[] excel) throws IOException{
        List<Person> excelBankids = getBankIdFromExcel(excel);
        List<Person> all = dao.findAll();
        Set<String> result = new HashSet<String>();
        for (Person person : excelBankids) {
            if(StringUtils.isBlank(person.getBankid())){
                continue;
            }
            boolean find = false;
            for (Person dbPerson : all) {
                if(dbPerson.getBankid().trim().equals(person.getBankid().trim())){
                    find = true;
                    break;
                }

            }
            if(!find){
                result.add(person.getBankid());
            }

        }

        return new ArrayList<String>(result);
    }

    private List<Person> getBankIdFromExcel(byte[] bytes) throws IOException{
        POIHelper<Person> poiHelper = POIHelper.newHelper();
        return poiHelper
                .objClass(Person.class)
                .sheetIndex(0)
                .workbook(bytes)
                .map("bankid", 0)
                .toObejct();
    }

    private List<Person> needInsertFromExcel(byte[] bytes) throws IOException {
        return new POIHelper<Person>().workbook(bytes)
                .objClass(Person.class)
                .sheetIndex(0)
                .map("name", 0)
                .map("sex", 1)
                .map("canji", 2)
                .map("identity", 3)
                .map("deformity", 4)
                .map("bankid", 5)
                .map("xingzhi", 6, new POIHelper.Converter() {
                    @Override
                    public Object convet(String s) {
                        if(s == null || s.equals("")){
                            return "2";
                        }
                        return "1";
                    }
                })
                .map("address", 7)
                .map("phonenumber", 8)
                .map("leixing", 9)
                .map("quyu", 10)
                .toObejct();
    }

    private List<Person> needUpdateFromExcel(byte[] bytes) throws IOException {
        return new POIHelper<Person>().workbook(bytes)
                .objClass(Person.class)
                .sheetIndex(1)
                .map("identity", 0)
                .map("zhuangtai", 1)
                .map("beizhu", 2)
                .toObejct();
    }

    @Override
    public List<String> listAllArea() {
        List<String> result = new ArrayList<String>();
        for (Person person : this.dao.listAllAreas()) {
            result.add(person.getQuyu());
        }
        return result;
    }

    @Override
    public void delete(String personId) {
        Person person = new Person();
        person.setId(personId);
        dao.delete(person);
    }
}
