package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.douban.SeriesEpisode;
import com.cosmax.media.system.commons.response.SeasonEpisode;
import com.cosmax.media.system.db.provider.mapper.SeriesEpisodeMapper;
import com.cosmax.media.system.db.api.SeriesEpisodeService;
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
public class SeriesEpisodeServiceImpl extends ServiceImpl<SeriesEpisodeMapper, SeriesEpisode> implements SeriesEpisodeService {

    @Resource
    private SeriesEpisodeMapper seriesEpisodeMapper;
    @Override
    public List<SeriesEpisode> getEpisodesInfosByLibraryId(Long id) {
        return seriesEpisodeMapper.getEpisodesInfosByLibraryId(id);
    }

    @Override
    public List<SeasonEpisode> getEpisodesInfosBySeriesId(Long id) {
        return seriesEpisodeMapper.getEpisodesInfosBySeriesId(id);
    }

    @Override
    public int deleteSeriesEpisodeByMediaIdAndSeriesIdAndEpisode(Long mediaId, String seriesName, Integer episode) {
        return seriesEpisodeMapper.deleteSeriesEpisodeByMediaIdAndSeriesIdAndEpisode(mediaId, seriesName, episode);
    }

    @Override
    public SeriesEpisode getEpisodesInfosBySeriesNameAndEpisode(Long id, String seriesName, Integer episode) {
        return seriesEpisodeMapper.getEpisodesInfosBySeriesNameAndEpisode(id, seriesName, episode);
    }
}
