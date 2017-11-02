package cn.itcast.bos.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gys on 2017/8/27.
 */
public final class POIUtils {
    private POIUtils(){}

    public static HSSFWorkbook fromBytes(byte[] bytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return new HSSFWorkbook(inputStream);
    }

}
