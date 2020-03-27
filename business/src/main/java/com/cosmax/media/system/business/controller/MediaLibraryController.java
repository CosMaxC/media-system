package com.cosmax.media.system.business.controller;


import com.cosmax.media.system.business.business.MediaLibraryBusiness;
import com.cosmax.media.system.business.dto.MediaLibraryDto;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.commons.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@RestController
@RequestMapping("/media")
public class MediaLibraryController {

    @Autowired
    private MediaLibraryBusiness mediaLibraryBusiness;

    @PostMapping("/insert")
    public BaseResult insert(@RequestBody MediaLibrary mediaLibrary){
        return mediaLibraryBusiness.insert(mediaLibrary);
    }

    @GetMapping("/{id}")
    public BaseResult getMediaBaseInfo(@PathVariable("id") Long id){
        return mediaLibraryBusiness.getMediaBaseInfo(id);
    }

    @PostMapping("/update")
    public BaseResult update(@RequestBody MediaLibraryDto dto){
        return mediaLibraryBusiness.update(dto);
    }

    @PostMapping("/delete/{id}")
    public BaseResult delete(@PathVariable("id") Long id){
        return mediaLibraryBusiness.delete(id);
    }

    @GetMapping("/infos")
    public BaseResult queryList(){
        return mediaLibraryBusiness.queryAll();
    }

    @GetMapping("/infos/{id}")
    public BaseResult queryInfoById(@PathVariable("id") Long id){
        return mediaLibraryBusiness.queryInfoById(id);
    }

    // 通过电影id获取详细信息含豆瓣
    @GetMapping("/movies/{id}")
    public BaseResult queryMovieAllInfos(@PathVariable("id") Long id){
        return mediaLibraryBusiness.queryMovieAllInfos(id);
    }

    // 通过剧集id获取详细信息含豆瓣
    @GetMapping("/series/{id}")
    public BaseResult querySeriesAllInfos(@PathVariable("id") Long id){
        return mediaLibraryBusiness.querySeriesAllInfos(id);
    }

    // 刷新媒体库信息
    @PostMapping("/infos/refresh/{id}")
    public BaseResult refreshMedia(@PathVariable("id") Long id){
        return mediaLibraryBusiness.refreshMedia(id);
    }

    // 刷新豆瓣信息
    @PostMapping("/douban/refresh/{id}")
    public BaseResult refreshDouban(@PathVariable("id") Long id){
        return mediaLibraryBusiness.refreshDouban(id);
    }

    @GetMapping("/echo")
    public String echo(){
        return FileUtils.getJarFilePath();
    }
}

