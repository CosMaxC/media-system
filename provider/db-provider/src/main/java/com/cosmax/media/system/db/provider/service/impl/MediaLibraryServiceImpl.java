package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.SeriesAndMovieQueryResponse;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.db.provider.mapper.MediaLibraryMapper;
import com.cosmax.media.system.db.api.MediaLibraryService;
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
public class MediaLibraryServiceImpl extends ServiceImpl<MediaLibraryMapper, MediaLibrary> implements MediaLibraryService {

    @Resource
    private MediaLibraryMapper mediaLibraryMapper;

    @Override
    public List<MediaLibrary> getScheduleList(Integer state) {
        return mediaLibraryMapper.getScheduleList(state);
    }

    @Override
    public MediaLibrary getLibraryByDoubanCondition() {
        return mediaLibraryMapper.getLibraryByDoubanCondition();
    }

    @Override
    public List<MediaLibrary> getFinishedLibrary() {
        return mediaLibraryMapper.getFinishedLibrary();
    }

    @Override
    public List<SeriesAndMovieQueryResponse> getSeriesAndMovieQueryResponseById(Long id) {
        return mediaLibraryMapper.getSeriesAndMovieQueryResponseById(id);
    }

    @Override
    public Boolean deleteMediaVideosAndDoubanInfosById(Long id) {
        try{
            mediaLibraryMapper.deleteMediaVideosAndDoubanInfosById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
