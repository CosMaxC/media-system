package com.cosmax.media.system.douban.provider.constants;

/**
 * @program: media-system
 * @description: url常量, 参考：https://douban-api-docs.zce.me/movie.html#search
 * @author: Cosmax
 * @create: 2020/02/05 16:47
 */
public class URLConstant {


    private static final String HTTPS = "https://";

    private static final String DOUBAN_API_VERSION = "/v2";

    // 豆瓣api
    private static final String DOUBAN_API = HTTPS + "api.douban.com" + DOUBAN_API_VERSION;

    /**
     * 电影条目信息
     * 使用方法: /v2/movie/subject/:id
     * method: get
     * example: /v2/movie/subject/1764796
     * status: 200
     */
    public static final String MOVIE_INFO = DOUBAN_API + "/movie/subject/:id";

    /**
     * 影人条目信息
     * 使用方法: /v2/movie/celebrity/:id
     * method: get
     * example: /v2/movie/celebrity/1054395
     * status: 200
     */
    public static final String CELEBRITY_INFO = DOUBAN_API + "/movie/celebrity/:id";

    /**
     * 电影条目剧照
     * 使用方法: /v2/movie/subject/:id/photos
     * method: get
     * example: /v2/movie/subject/1054395/photos
     * status: 200
     */
    public static final String MOVIE_PHOTO_INFO = DOUBAN_API + "/movie/subject/:id/photos";

    /**
     * 电影条目影评列表
     * 使用方法: /v2/movie/subject/:id/reviews
     * method: get
     * example: /v2/movie/subject/1054395/reviews
     * status: 200
     */
    public static final String REVIEWS_INFO = DOUBAN_API + "/movie/subject/:id/reviews";

    /**
     * 电影条目短评列表
     * 使用方法: /v2/movie/subject/:id/comments
     * method: get
     * example: /v2/movie/subject/1054395/comments
     * status: 200
     */
    public static final String COMMENTS_INFO = DOUBAN_API + "/movie/subject/:id/comments";

    /**
     * 影人作品
     * 使用方法: /v2/movie/celebrity/:id/works
     * method: get
     * example: /v2/movie/celebrity/1054395/works
     * status: 200
     */
    public static final String CELEBRITY_WORKS_INFO = DOUBAN_API + "/movie/celebrity/:id/works";

    /**
     * 影人剧照
     * 使用方法: /v2/movie/celebrity/:id/photos
     * method: get
     * example: /v2/movie/celebrity/1054395/photos
     * status: 200
     */
    public static final String CELEBRITY_PHOTOS_INFO = DOUBAN_API + "/movie/celebrity/:id/photos";

    /**
     * 电影条目搜索
     * 使用方法: /v2/movie/search?q={text}
     * method: get
     * example: /v2/movie/search?q=张艺谋
     * status: 200
     */
    public static final String MOVIE_SEARCH = DOUBAN_API + "/movie/search";

    /**
     * top250
     * 使用方法: /v2/movie/top250
     * method: get
     * status: 200
     */
    public static final String MOVIE_TOP250 = DOUBAN_API + "/movie/top250";

    /**
     * 北美票房榜
     * 使用方法: /movie/us_box
     * method: get
     * status: 200
     */
    public static final String MOVIE_US_BOX = DOUBAN_API + "/movie/us_box";

    /**
     * 口碑榜
     * 使用方法: /v2/movie/weekly
     * method: get
     * status: 200
     */
    public static final String MOVIE_WEEKLY = DOUBAN_API + "/movie/weekly";

    /**
     * 新片榜
     * 使用方法: /v2/movie/new_movies
     * method: get
     * status: 200
     */
    public static final String MOVIE_NEW_MOVIES = DOUBAN_API + "/movie/new_movies";

    /**
     * 正在上映
     * 使用方法: /v2/movie/in_theaters
     * method: get
     * status: 200
     */
    public static final String MOVIE_IN_THEATERS = DOUBAN_API + "/movie/in_theaters";

    /**
     * 即将上映
     * 使用方法: /v2/movie/coming_soon
     * method: get
     * status: 200
     */
    public static final String MOVIE_COMING_SOON = DOUBAN_API + "/movie/coming_soon";

}
