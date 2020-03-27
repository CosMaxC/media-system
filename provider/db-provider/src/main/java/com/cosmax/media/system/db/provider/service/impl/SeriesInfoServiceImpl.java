package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.douban.SeriesInfo;
import com.cosmax.media.system.db.provider.mapper.SeriesInfoMapper;
import com.cosmax.media.system.db.api.SeriesInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Service
public class SeriesInfoServiceImpl extends ServiceImpl<SeriesInfoMapper, SeriesInfo> implements SeriesInfoService {

    @Resource
    private SeriesInfoMapper seriesInfoMapper;
    @Override
    public List<SeriesInfo> getSeriesInfosByLibraryId(Long id) {
        return seriesInfoMapper.getSeriesInfosByLibraryId(id);
    }

    @Override
    public void deleteByMediaId(Long mediaId) {
        seriesInfoMapper.deleteByMediaId(mediaId);
    }
}
