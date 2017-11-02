package cn.itcast.bos.service.business;

import cn.itcast.bos.domain.business.PlayMoneyRecord;

import java.io.IOException;
import java.util.List;

/**
 * Created by gys on 2017/8/28.
 */
public interface PlayMoneyService {

    List<PlayMoneyRecord> listPlayMoney(String bankId);

    void importExcel(byte[] bytes, String date) throws IOException;
}
