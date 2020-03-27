package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.douban.Star;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
public interface StarMapper extends BaseMapper<Star> {

    /**
     * 获取导演list
     * @param id 豆瓣id
     * @return List<Star> 导演list
     */
    List<Star> getDirectorListByVideoId(@Param("id") Long id);

    /**
     * 获取编剧list
     * @param id 豆瓣id
     * @return List<Star> 编剧list
     */
    List<Star> getWriterListByVideoId(@Param("id") Long id);
    /**
     * 获取演员list
     * @param id 豆瓣id
     * @return List<Star> 演员list
     */
    List<Star> getStarListByVideoId(@Param("id") Long id);
}
