package cn.itcast.bos.service.business.impl;

import cn.itcast.bos.dao.business.PersonDao;
import cn.itcast.bos.domain.business.Person;
import cn.itcast.bos.service.business.PersonService;
import cn.itcast.bos.utils.POIHelper;
import cn.itcast.bos.utils.UUIDUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        /*
         * 修改规则:
          * 状态有效值为1， 2， 3
          * 1为死亡直接处理
          * 2为困难取消， 当为2时， 如果残疾类型为困难时， 状态变更为取消， 如果残疾类型为重度&困难时， 残疾类型修改为
          * 困难，状态仍为正常
          * 3为重度取消， 当状态为取消时，状态直接修改为取消
         */
        for (Person person : needUpdate) {
            if("1".equals(person.getZhuangtai())){
                // do nothing
                dao.update(person);
                continue;
            }
            if("2".equals(person.getZhuangtai())){
                Person condition = new Person();
                condition.setIdentity(person.getIdentity());
                List<Person> personList = dao.findByCondition(condition);
                if(personList == null || personList.isEmpty()){
                    continue;
                }
                for (Person _person : personList) {
                    person.setId(_person.getId());
                    // 取消困难， 直接取消
                    if("1".equals(_person.getLeixing())){
                        person.setZhuangtai("2");
                        dao.update(person);
                    }
                    if("3".equals(_person.getLeixing())){
                        // 对状态不做修改
                        person.setZhuangtai(null);
                        // 修改残疾等级为重度
                        person.setLeixing("2");
                        dao.update(person);
                    }
                    if("2".equals(_person.getLeixing())){
                        // 对重度用户不做操作
                        person.setZhuangtai(null);
                        dao.update(person);
                    }
                }
                continue;
            }

            // 直接修改状态为取消
            if("3".equals(person.getZhuangtai())){
                person.setZhuangtai("2");
                dao.update(person);
            }
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

    @Override
    public long compact() {
        Person condition = new Person();
        condition.setRepeatFlag("1");
        List<Person> people = listAllPerson(condition);
        if(people == null || people.isEmpty()){
            return 0L;
        }
        long result = 0L;
        ArrayListMultimap<CompactKey, Person> map = ArrayListMultimap.create();
        for (Person person : people) {
            map.put(new CompactKey(person.getIdentity(), person.getDeformity(), person.getBankid()), person);
        }

        for (CompactKey key : map.keySet()) {
            List<Person> personList = map.get(key);
            if(personList.size() <= 1){
                continue;
            }
            long l;
            try {
                l = compactRecord(personList);
            } catch (ParseException e) {
                // 错误数据
                continue;
            }
            result += l;
        }
        return result;
    }

    /**
     * 合并记录， 找出最后上传的， 修改残疾等级，然后删除其它
     * @param personList
     * @return
     */
    private long compactRecord(List<Person> personList) throws ParseException {
        Person lastOne = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lastDate = null;
        for (Person person : personList) {
            if(lastOne == null){
                lastOne = person;
                if(person.getUploadDate() != null){
                    lastDate = sdf.parse(person.getUploadDate());
                }
            }
            if(person.getUploadDate() == null){
                continue;
            }
            Date date = sdf.parse(person.getUploadDate());
            if(lastDate == null){
                lastDate = date;
                lastOne = person;
            }

            if(!person.equals(lastOne) && lastDate.equals(date)){
                // 如果有两条记录都是同一天上传， 则不做处理
                return 0L;
            }
            if(lastDate.before(date)){
                lastDate = date;
                lastOne = person;
            }
        }
        if(lastOne == null){
            return 0L;
        }
        // 最后为困难&重度， 直接返回
        if(lastOne.getLeixing().equals("3")){
            personList.remove(lastOne);
            update(lastOne);
            removeList(personList);
            return personList.size() + 1;
        }

        caculateLevel(lastOne, personList);
        personList.remove(lastOne);
        update(lastOne);
        removeList(personList);

        return personList.size() + 1;
    }

    /**
     * 计算残疾等级
     * @param result
     * @param personList
     */
    private void caculateLevel(Person result, List<Person> personList){
        String leixing = result.getLeixing();

        for (Person person : personList) {
            if(person.getLeixing().equals("3")){
                result.setLeixing("3");
                break;
            }
            if(!person.getLeixing().equals(leixing)){
                result.setLeixing("3");
                break;
            }
        }
    }

    private void removeList(List<Person> list){
        for (Person person : list) {
            delete(person.getId());
        }
    }

    private class CompactKey{
        private String identity;

        private String deformity;

        private String bankId;

        public CompactKey(String identity, String deformity, String bankId) {
            this.identity = identity;
            this.deformity = deformity;
            this.bankId = bankId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompactKey that = (CompactKey) o;

            if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
            if (deformity != null ? !deformity.equals(that.deformity) : that.deformity != null) return false;
            return bankId != null ? bankId.equals(that.bankId) : that.bankId == null;
        }

        @Override
        public int hashCode() {
            int result = identity != null ? identity.hashCode() : 0;
            result = 31 * result + (deformity != null ? deformity.hashCode() : 0);
            result = 31 * result + (bankId != null ? bankId.hashCode() : 0);
            return result;
        }
    }
}
