package com.cosmax.media.system.test;

import com.alibaba.fastjson.JSON;
import com.cosmax.media.system.MediaSystemApplication;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.commons.domain.douban.MediaVideoDouban;
import com.cosmax.media.system.commons.domain.douban.Star;
import com.cosmax.media.system.commons.response.DoubanResponse;
import com.cosmax.media.system.commons.response.MovieResponse;
import com.cosmax.media.system.commons.response.SeasonEpisode;
import com.cosmax.media.system.db.api.*;
import com.cosmax.media.system.db.provider.mapper.SeriesEpisodeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: media-system
 * @description:
 * @author: Cosmax
 * @create: 2020/02/04 16:32
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediaSystemApplication.class)
public class DbTest {

    @Autowired
    private MediaLibraryService mediaLibraryService;

    @Resource
    private StarService starService;

    @Resource
    private MediaVideoDoubanService mediaVideoDoubanService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private DoubanInfoService doubanInfoService;

    @Autowired
    private SeriesEpisodeMapper mapper;


    @Test
    public void insertTest(){
//        MediaLibrary mediaLibrary = new MediaLibrary();
//        mediaLibrary.setDirectory("E:\\");
//        mediaLibrary.setId(100101001001L);
//        mediaLibrary.setName("电影");
//        mediaLibrary.setType(0);
////        mediaLibrary.setIsSearch(0);
//        System.out.println(mediaLibraryService.saveOrUpdate(mediaLibrary));

//        DoubanResponse doubanResponseByVideosId = doubanInfoService.getDoubanResponseByVideosId(1001L);
        List<SeasonEpisode> episodesInfosBySeriesId = mapper.getEpisodesInfosBySeriesId(1001L);
//        MovieResponse movieResponseById = movieService.getMovieResponseById(1001L);
        System.out.println(JSON.toJSONString(episodesInfosBySeriesId));
//        Star star = new Star();
//        star.setStarId(1001L);
//        star.setStarName("asssin111");
//        star.setStarImg("test");
//        star.setStarBrief("111111111");
//
//        System.out.println(starService.saveOrUpdate(star));

//        MediaVideoDouban mediaVideoDouban = new MediaVideoDouban();
//        mediaVideoDouban.setMediaId(1000233L);
//        mediaVideoDouban.setItemId(11111L);
//        mediaVideoDouban.setDoubanId(33332313123L);
//        System.out.println(mediaVideoDoubanService.saveOrUpdate(mediaVideoDouban));
//
    }
}
