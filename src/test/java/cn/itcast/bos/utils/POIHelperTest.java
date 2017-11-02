package cn.itcast.bos.utils;

import cn.itcast.bos.domain.business.Person;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gys on 2017/8/27.
 */
public class POIHelperTest {

    byte[] bytes = null;

    @Before
    public void before() throws Exception{
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xls");
        bytes = IOUtils.toByteArray(inputStream);
    }

    @Test
    public void toObejct() throws Exception {
        POIHelper<Person> poiHelper = POIHelper.newHelper();
        poiHelper.objClass(Person.class)
                .skipRows(0)
                .sheetIndex(0)
                .map("id", 0)
                .map("name", 1)
                .map("sex", 2)
                .map("identity", 4)
                .workbook(bytes);

        List<Person> list = poiHelper.toObejct();
        System.out.println(list);
    }

}