package com.cosmax.media.system.db.provider.mapper;

import com.cosmax.media.system.commons.domain.DoubanAllInfo;
import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cosmax.media.system.commons.response.DoubanResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Mapper
@Component
public interface DoubanInfoMapper extends BaseMapper<DoubanInfo> {
    /**
     * 根据媒体库id豆瓣信息
     * @param id 媒体库id
     * @return 豆瓣信息实体
     */
    @Select("SELECT " +
            "di.douban_id, " +
            "di.star_count, " +
            "di.score, " +
            "di.made_by, " +
            "di.`year`, " +
            "di.cost_time, " +
            "di.on_time, " +
            "di.eng_name, " +
            "di.douban_link, " +
            "di.avatar_path, " +
            "di.poster_path, " +
            "di.sub_type, " +
            "di.cn_name, " +
            "di.brief, " +
            "GROUP_CONCAT(CONCAT(s1.star_id,'|',s1.star_name,'|',s1.star_img)) AS writerInfo, " +
            "GROUP_CONCAT(CONCAT(s2.star_id,'|',s2.star_name,'|',s2.star_img)) AS starInfo, " +
            "GROUP_CONCAT(CONCAT(s3.star_id,'|',s3.star_name,'|',s3.star_img)) AS directorInfo " +
            "FROM " +
            "douban_info AS di " +
            "LEFT JOIN douban_star AS ds ON di.douban_id = ds.douban_id " +
            "LEFT JOIN douban_writer AS dw ON di.douban_id = dw.douban_id " +
            "LEFT JOIN douban_director AS dd ON di.douban_id = dd.douban_id " +
            "LEFT JOIN star AS s1 ON s1.star_id = dw.writer_id AND di.douban_id = s1.star_id " +
            "LEFT JOIN star AS s2 ON ds.star_id = s2.star_id " +
            "LEFT JOIN star AS s3 ON dd.director_id = s3.star_id " +
            "INNER JOIN media_video_douban mvd ON di.douban_id = mvd.douban_id " +
            "WHERE " +
            "mvd.media_id = #{id} " +
            "GROUP BY  " +
            " di.douban_id, " +
            " di.star_count, " +
            " di.score, " +
            " di.made_by, " +
            " di.`year`, " +
            " di.cost_time, " +
            " di.on_time, " +
            " di.eng_name, " +
            " di.douban_link, " +
            " di.avatar_path, " +
            " di.poster_path, " +
            " di.sub_type, " +
            " di.cn_name, " +
            " di.brief, " +
            " s1.star_id, " +
            " s1.star_name, " +
            " s1.star_img, " +
            " s2.star_id, " +
            " s2.star_name, " +
            " s2.star_img, " +
            " s3.star_id, " +
            " s3.star_name, " +
            " s3.star_img ")
    DoubanAllInfo getDoubanInfosByLibraryId(@Param("id") Long id);

    /**
     * 根据视频id豆瓣信息
     * @param id 视频id
     * @return 豆瓣返回信息实体
     */
    DoubanResponse getDoubanResponseByVideosId(@Param("id") Long id);
}
