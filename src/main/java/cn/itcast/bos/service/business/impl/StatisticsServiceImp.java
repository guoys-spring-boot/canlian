package cn.itcast.bos.service.business.impl;

import cn.itcast.bos.dao.business.StatisticsDao;
import cn.itcast.bos.domain.business.PlayMoneyStaticticsBean;
import cn.itcast.bos.service.business.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gys on 2017/8/27.
 */

@Service
public class StatisticsServiceImp implements StatisticsService {

    @Autowired
    private StatisticsDao dao;

    @Override
    public List<PlayMoneyStaticticsBean> playMoneyStatistics(PlayMoneyStaticticsBean bean){
        return dao.playMoneyStatistics(bean);
    }

    @Override
    public long countPlayMoneyStatistics(PlayMoneyStaticticsBean bean) {
        return dao.countPlayMoneyStatistics(bean);
    }
}
