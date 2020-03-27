package com.cosmax.media.system.douban.provider.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cosmax.media.system.commons.domain.DoubanVideoInfo;
import com.cosmax.media.system.commons.domain.SubTypeEnum;
import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import com.cosmax.media.system.commons.domain.douban.Star;
import com.cosmax.media.system.commons.utils.FileUtils;
import com.cosmax.media.system.douban.provider.constants.URLConstant;
import org.apache.commons.lang3.StringUtils;
import com.cosmax.media.system.douban.provider.biz.DoubanRequestBiz;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: media-system
 * @description: 豆瓣接口实现
 * @author: Cosmax
 * @create: 2020/02/07 12:18
 */

@Component
public class DoubanRequestBizImpl implements DoubanRequestBiz {
    @Override
    public Star getStarByStarId(Long id, String apikey) {

        Star star = new Star();

        // 校验id和apikey参数合理化
        if (!checkIdAndApikey(id, apikey)){
            return null;
        }
        // 获取演员json
        JSONObject request = getRequestByIdAndApikey(URLConstant.CELEBRITY_INFO, id, apikey);
        // 校验响应是否正常 有code不正常
        if (request.containsKey("code")){
            return null;
        }

        // 演员id
        star.setStarId(id);

        // 演员名
        star.setStarName(request.getString("name"));

        // 演员简要介绍
        star.setStarBrief(request.getString("summary"));

        // 获取演员照片
        JSONObject avatars = request.getJSONObject("avatars");
        if (avatars != null){
            String imagesUrl = avatars.getString("large");
            // 路径为运行文件目录/photos/豆瓣id
            String urlPath = "/photos/stars/" + "star_" + id ; // 网络地址
            String downLoadPath = FileUtils.getJarFilePath() + "/photos/stars"; // 真实地址
            star.setStarImg(FileUtils.downloadJPEGFromUrl(imagesUrl, downLoadPath, "star_" + id, urlPath));
        }

        return star;
    }

    @Override
    public DoubanVideoInfo getDoubanInfoByDoubanId(Long id, String apikey) {

        // 校验id和apikey参数合理化
        if (!checkIdAndApikey(id, apikey)){
            return null;
        }
        // 获取影视json
        JSONObject request = getRequestByIdAndApikey(URLConstant.MOVIE_INFO, id, apikey);

        // 校验响应是否正常 有code不正常
        if (request.containsKey("code")){
            return null;
        }

        DoubanVideoInfo doubanVideoInfo = new DoubanVideoInfo();

        // 获取豆瓣基本信息
        DoubanInfo doubanInfo = getDoubanInfoBySubjectJSON(request, id, apikey);
        doubanVideoInfo.setDoubanInfo(doubanInfo);

        // 获取导演
        JSONArray directors = request.getJSONArray("directors");
        doubanVideoInfo.setDirectorList(getIdsByJSONArray(directors));


        // 获取编剧
        JSONArray writers = request.getJSONArray("writers");
        doubanVideoInfo.setWriterList(getIdsByJSONArray(writers));

        // 获取演员
        JSONArray casts = request.getJSONArray("casts");
        doubanVideoInfo.setActorList(getIdsByJSONArray(casts));

        return doubanVideoInfo;
    }

    /**
     * 通过json获取豆瓣信息
     * @param request  豆瓣movie api {@link JSONObject}
     * @param id 豆瓣id
     * @param apikey 密钥
     * @return 豆瓣信息 {@link DoubanInfo}
     */
    private DoubanInfo getDoubanInfoBySubjectJSON(JSONObject request, Long id, String apikey) {
        DoubanInfo doubanInfo = new DoubanInfo();
        // 豆瓣基础信息
        doubanInfo.setDoubanId(id);

        // 豆瓣简要信息
        doubanInfo.setBrief(request.getString("summary"));

        // 封面照片
        String imagesUrl = request.getJSONObject("images").getString("large");

        // 运行文件目录下创建图片文件夹保存豆瓣图片
        // 路径为运行文件目录/photos/豆瓣id
        String avatarUrlPath = "/photos/videos/" + "avatar_" + id; // 网络地址
        String downLoadPath = FileUtils.getJarFilePath() + "/photos/videos"; // 真实地址
        doubanInfo.setAvatarPath(FileUtils.downloadJPEGFromUrl(imagesUrl, downLoadPath, "avatar_" + id, avatarUrlPath));

        // 剧照请求
        JSONObject photosRequest = getRequestByIdAndApikey(URLConstant.MOVIE_PHOTO_INFO, id, apikey);
        JSONArray photos = photosRequest.getJSONArray("photos");
        if (photos.size() != 0){
            // 获取第一张
            String thumbUrl = photos.getJSONObject(0).getString("image");
            String posterUrlPath = "/photos/videos/" + "poster_" + id;
            doubanInfo.setPosterPath(FileUtils.downloadJPEGFromUrl(thumbUrl, downLoadPath, "poster_" + id, posterUrlPath));
        }

        // 译名
        doubanInfo.setEngName(request.getString("original_title"));

        // 中文名
        doubanInfo.setCnName(request.getString("title"));

        // 获取年份
        doubanInfo.setYear(request.getInteger("year"));

        // 豆瓣链接
        doubanInfo.setDoubanLink(request.getString("alt"));

        // 电影/电视剧单集长度时间
        JSONArray durationsArr = request.getJSONArray("durations");
        doubanInfo.setCostTime(getStringByJSONArray(durationsArr));

        // 电视剧/电影类型
        if (SubTypeEnum.MOVIE.getName().equals(request.getString("subtype"))) {
            doubanInfo.setSubType(SubTypeEnum.MOVIE.getId());
        }else {
            doubanInfo.setSubType(SubTypeEnum.TV.getId());
        }

        // 获取上映时间
        JSONArray pubdatesArr = request.getJSONArray("pubdates");
        doubanInfo.setOnTime(getStringByJSONArray(pubdatesArr));


        // 获取国家
        JSONArray countryArr = request.getJSONArray("countries");
        doubanInfo.setMadeBy(getStringByJSONArray(countryArr));

        // 评分json
        JSONObject rating = request.getJSONObject("rating");
        doubanInfo.setScore(rating.getFloat("average"));
        doubanInfo.setStarCount(rating.getInteger("stars"));

        return doubanInfo;
    }

    /**
     * 需要改id的公共方法
     * @param url URLConstant内带id的参数 {@link URLConstant}
     * @param id 豆瓣id
     * @param apikey 密钥
     * @return 返回实体 {@link JSONObject}
     */
    private JSONObject getRequestByIdAndApikey(String url, Long id, String apikey){
        url = url.replaceAll(":id", id.toString()) + "?apikey=" + apikey;
        WebClient webClient = WebClient.create();
        return webClient.get().uri(url).retrieve().bodyToMono(JSONObject.class).block();


    }

    /**
     * 校验id和apikey参数合理化
     * @param id 豆瓣id
     * @param apikey 密钥
     * @return 返回true
     */
    private Boolean checkIdAndApikey(Long id, String apikey){
        return id != null && id > 0 && !StringUtils.isBlank(apikey);
    }

    /**
     * 拼接array成字符串操作
     * @param jsonArray {@link JSONArray}
     * @return String
     */
    private String getStringByJSONArray(JSONArray jsonArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            stringBuilder.append(jsonArray.getString(i)).append("/");
        }
        String string = stringBuilder.toString();
        return string.length() > 0 ? string.substring(0,string.length()-1) : "";
    }

    /**
     * 从array获取id操作
     * @param jsonArray {@link JSONArray}
     * @return String id 集合
     */
    private List<Long> getIdsByJSONArray(JSONArray jsonArray){
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Long id = jsonArray.getJSONObject(i).getLong("id");
            if (id != null) {
                list.add(id);
            }
        }
        return list;
    }


}
