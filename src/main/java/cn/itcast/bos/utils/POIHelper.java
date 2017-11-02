package cn.itcast.bos.utils;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.base.Preconditions.*;

/**
 * Created by gys on 2017/8/27.
 */
public class POIHelper<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public POIHelper() {
    }

    private int sheetIndex;

    private int skipRows;

    private int currentRow;

    private int totalRows;

    private Workbook workbook;

    private Class<? extends T> objClass;

    private List<Processor> processors = new ArrayList<Processor>();

    private Sheet sheet;

    private Map<String, CellConfigEntry> fieldMap = new HashMap<String, CellConfigEntry>();

    public static <T> POIHelper<T> newHelper() {
        return new POIHelper<T>();
    }

    /**
     * 默认使用03版的excel
     * @param bytes 03excel的byte文件
     * @return this
     * @throws IOException
     */
    public POIHelper<T> workbook(byte[] bytes) throws IOException {
        this.workbook = POIUtils.fromBytes(bytes);
        return this;
    }

    public POIHelper<T> workbook(Workbook workbook){
        this.workbook = workbook;
        return this;
    }

    public POIHelper<T> sheetIndex(int index) {
        this.sheetIndex = index;
        return this;
    }

    public POIHelper<T> skipRows(int skipRows) {
        this.skipRows = skipRows;
        return this;
    }

    public POIHelper<T> registerProcessor(Processor processor) {
        this.processors.add(processor);
        return this;
    }

    public POIHelper<T> map(String fieldName, Integer columnIndex) {
        CellConfigEntry entry = new CellConfigEntry();
        entry.columnIndex = columnIndex;
        entry.filedName = fieldName;
        entry.converter = new DefaultConverter();
        this.fieldMap.put(fieldName, entry);
        return this;
    }

    public POIHelper<T> map(String fieldName, Integer columnIndex, Converter converter) {
        CellConfigEntry entry = new CellConfigEntry();
        entry.columnIndex = columnIndex;
        entry.filedName = fieldName;
        entry.converter = converter;
        this.fieldMap.put(fieldName, entry);
        return this;
    }

    public POIHelper<T> objClass(Class<? extends T> clazz) {
        this.objClass = clazz;
        return this;
    }

    public List<T> toObejct() {
        initAndCheck();
        List<T> result = new ArrayList<T>();
        Row row;
        while ((row = currentRow()) != null) {
            T t = newInstace();
            fillObj(t, row);
            result.add(t);
        }
        return result;
    }

    private void initAndCheck() {
        checkNotNull(this.objClass);
        checkNotNull(this.workbook);
        checkState(this.skipRows >= 0);
        checkState(this.sheetIndex >= 0);
        this.sheet = workbook.getSheetAt(sheetIndex);
        this.currentRow = skipRows;
        this.totalRows = sheet.getPhysicalNumberOfRows();
    }

    private Row currentRow() {
        if (currentRow < totalRows) {
            Row row = sheet.getRow(currentRow);
            currentRow++;
            return row;
        }
        return null;
    }

    private void fillObj(T t, Row row) {
        for (Map.Entry<String, CellConfigEntry> entry : this.fieldMap.entrySet()) {
            try {
                Field field = objClass.getDeclaredField(entry.getKey());
                Cell cell = row.getCell(entry.getValue().columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String s = getCellString(cell);
                beforeSetValue();
                setFieldValue(entry.getValue(), s, field, t);
                afterSetValue();
            } catch (NoSuchFieldException e) {
                logger.warn("{}属性不存在", entry.getKey());
            }
        }
    }

    private void setFieldValue(CellConfigEntry entry, String value, Field field, T t){
        Object o = entry.converter.convet(value);
        field.setAccessible(true);
        try {
            field.set(t, o);
        } catch (IllegalAccessException e) {
            // do nothing
        }
    }

    private void beforeSetValue(){

    }

    private void afterSetValue(){

    }

    private String getCellString(Cell cell) {
        String str;

        switch (cell.getCellType()) {//判断单元格类型
            case Cell.CELL_TYPE_NUMERIC:
                double value = cell.getNumericCellValue();
                if(value == (int)value){
                    str = String.valueOf((int)value);
                    break;
                }
                str = String.valueOf(value);
                break;
            case Cell.CELL_TYPE_STRING:
                str = cell.getRichStringCellValue().toString();
                break;
            case Cell.CELL_TYPE_FORMULA:
                double value2 = cell.getNumericCellValue();
                if(value2 == (int)value2){
                    str = String.valueOf((int)value2);
                }else {
                    str = String.valueOf(value2);
                }
                if (str.equals("NaN")) {
                    str = cell.getRichStringCellValue().toString();
                }
                break;
            default:
                str = cell.getRichStringCellValue().toString();
        }
        return str;
    }

    private T newInstace() {
        try {
            return objClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("无效的obeClass:" + objClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("无效的obeClass:" + objClass.getName(), e);
        }
    }

    private class CellConfigEntry {
        private int columnIndex;
        private String filedName;
        private Converter converter;
    }

    private class DefaultConverter implements Converter {
        @Override
        public Object convet(String s) {
            return s;
        }
    }


    public interface Processor {
        void beforeSetValue();

        void afterSetValue();
    }

    public interface Converter {
        Object convet(String s);
    }
}
