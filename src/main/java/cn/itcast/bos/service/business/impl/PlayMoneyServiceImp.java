package cn.itcast.bos.service.business.impl;

import cn.itcast.bos.dao.business.PlayMoneyDao;
import cn.itcast.bos.domain.business.PlayMoneyRecord;
import cn.itcast.bos.service.business.PlayMoneyService;
import cn.itcast.bos.utils.POIHelper;
import cn.itcast.bos.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gys on 2017/8/28.
 */

@Service
@Transactional
public class PlayMoneyServiceImp implements PlayMoneyService {

    @Autowired
    private PlayMoneyDao moneyDao;

    private static final int BATCH_SIZE = 500;

    @Override
    public List<PlayMoneyRecord> listPlayMoney(String bankId){
        PlayMoneyRecord record = new PlayMoneyRecord();
        record.setBankid(bankId);
        return moneyDao.findByCondition(record);
    }

    @Override
    public void importExcel(byte[] bytes, String date) throws IOException {
        List<PlayMoneyRecord> moneyRecords = needInsertFromExcel(bytes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date _date = sdf.parse(date);
            List<PlayMoneyRecord> list = new ArrayList<PlayMoneyRecord>(BATCH_SIZE);
            for (PlayMoneyRecord moneyRecord : moneyRecords) {

                if(StringUtils.isBlank(moneyRecord.getBankid())){
                    continue;
                }
                moneyRecord.setId(UUIDUtils.generatePrimaryKey());
                moneyRecord.setDate(_date);
                list.add(moneyRecord);
                if(list.size() == BATCH_SIZE){
                    moneyDao.batchInsert(list);
                    list.clear();
                }
            }
            moneyDao.batchInsert(list);
        } catch (ParseException e) {
            throw new IOException("日期格式错误", e);
        }

    }

    private List<PlayMoneyRecord> needInsertFromExcel(byte[] bytes) throws IOException {
        return new POIHelper<PlayMoneyRecord>().workbook(bytes)
                .objClass(PlayMoneyRecord.class)
                .sheetIndex(0)
                .map("bankid", 0)
                .map("bankname", 1)
                .map("money", 2, new POIHelper.Converter() {
                    @Override
                    public Object convet(String s) {
                        if(StringUtils.isBlank(s)){
                            return 0d;
                        }
                        return Double.parseDouble(s);
                    }
                })
                .map("identity", 3)
                .toObejct();
    }
}
