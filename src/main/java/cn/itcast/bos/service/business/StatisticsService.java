package cn.itcast.bos.service.business;

import cn.itcast.bos.domain.business.PlayMoneyStaticticsBean;

import java.util.List;

/**
 * Created by gys on 2017/8/27.
 */
public interface StatisticsService {
    List<PlayMoneyStaticticsBean> playMoneyStatistics(PlayMoneyStaticticsBean bean);

    long countPlayMoneyStatistics(PlayMoneyStaticticsBean bean);
}
