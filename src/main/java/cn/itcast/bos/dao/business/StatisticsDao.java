package cn.itcast.bos.dao.business;

import cn.itcast.bos.domain.business.PlayMoneyRecord;
import cn.itcast.bos.domain.business.PlayMoneyStaticticsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by gys on 2017/8/27.
 */

@Mapper
public interface StatisticsDao {

    List<PlayMoneyStaticticsBean> playMoneyStatistics(PlayMoneyStaticticsBean record);

    long countPlayMoneyStatistics(PlayMoneyStaticticsBean bean);
}
