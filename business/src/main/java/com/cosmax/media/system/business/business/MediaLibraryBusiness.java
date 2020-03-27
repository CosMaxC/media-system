package com.cosmax.media.system.business.business;

import com.cosmax.media.system.business.dto.MediaLibraryDto;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;

import java.util.List;

/**
 * @program: media-system
 * @description: 媒体库调用api
 * @author: Cosmax
 * @create: 2020/03/02 13:54
 */
public interface MediaLibraryBusiness {

    /**
     * 新增媒体库
     * @param mediaLibrary 媒体库参数 {@link MediaLibrary}
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult insert(MediaLibrary mediaLibrary);

    /**
     * 更新媒体库
     * @param dto 更改媒体库参数 {@link MediaLibraryDto}
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult update(MediaLibraryDto dto);

    /**
     * 删除媒体库
     * @param id 删除id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult delete(Long id);

    /**
     * 查询所有媒体库信息
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult queryAll();


    /**
     * 根据id查询媒体库信息
     * @param id 媒体库id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult queryInfoById(Long id);

    /**
     * 根据电影id查询电影所有信息
     * @param id 电影id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult queryMovieAllInfos(Long id);

    /**
     * 根据剧集id查询电影所有信息
     * @param id 剧集id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult querySeriesAllInfos(Long id);

    /**
     * 将路径list的视频文件添加到媒体库
     * @param mediaLibrary 媒体库信息
     * @param filePath 文件路径list
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult insertVideoToMediaLibrary(MediaLibrary mediaLibrary, String filePath);

    /**
     * 将路径的视频文件从媒体库删除
     * @param mediaLibrary 媒体库信息
     * @param filePath 文件路径
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult deleteVideoToMediaLibrary(MediaLibrary mediaLibrary, String filePath);

    /**
     * 通过id获取媒体库基本信息
     * @param id 媒体库id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult getMediaBaseInfo(Long id);

    /**
     * 根据id刷新媒体库--删除媒体库的数据重新检查
     * @param id 媒体库id
     * @return 响应实体 {@link BaseResult}
     */
    BaseResult refreshMedia(Long id);

    /**
     * 根据id刷新豆瓣任务表
     * @param id 媒体库id
     * @return refreshDouban
     */
    BaseResult refreshDouban(Long id);
}
