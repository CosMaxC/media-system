package com.cosmax.media.system.test;

import com.alibaba.fastjson.JSON;
import com.cosmax.media.system.MediaSystemApplication;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.MovieInfo;
import com.cosmax.media.system.douban.api.DouBanRequestService;
import com.cosmax.media.system.file.api.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: media-system
 * @description: 豆瓣测试类
 * @author: Cosmax
 * @create: 2020/02/05 11:20
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediaSystemApplication.class)
public class DoubanRequestTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private DouBanRequestService douBanRequestService;

    @Test
    public void doubanGetTest(){
//        String url = "https://api.douban.com/v2/movie/search";
//        Map<String, Object> map = new HashMap<>();
//        map.put("q", "Agents of S H I E L D S01");
//        StringEscapeUtils.unescapeJava()
//        map.put("apikey", "02646d3fb69a52ff072d47bf23cef8fd");
//        System.out.println(HttpUtils.getRequest(url,map));
//        String urlencode = "Agents%20of%20S%20H%20I%20E%20L%20D%20S01";
//        String urlDecodeString = HttpUtils.getURLDecodeString(urlencode);
//        System.out.println(urlDecodeString);
//        System.out.println(HttpUtils.getURLEncodeString(urlDecodeString));

//        System.out.println(JSON.toJSONString(douBanRequestService.getFilePath()));

//        String url = "https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1398762158.38.webp";
//        String s = FileUtils.downloadFromUrl(url, "D:\\media-photos", "photo1", "jpeg");
//        System.out.println(s);
//        System.out.println(FileUtils.downloadJPEGFromUrl("https://img9.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1558448405.4.webp","aaa"));
//        System.out.println(FileUtils.downloadJPEGFromUrl("https://img3.doubanio.com/f/movie/63acc16ca6309ef191f0378faf793d1096a3e606/pics/movie/celebrity-default-large.png","aaa"));

//        File file = n

//        System.out.println(System.getProperty("user.home"));
//        System.out.println(FileUtils.getDefaultDownloadPath());
//  realPath = realPath.substring(0, realPath.indexOf(separator));

//        Map<String, Object> map = new HashMap<>();
//        map.put("q", DoubanHttpUtils.getURLEncodeString("Agents of S H I E L D 第1季"));
//        map.put("apikey", "02646d3fb69a52ff072d47bf23cef8fd");
//        String request = DoubanHttpUtils.getRequest(URLConstant.MOVIE_SEARCH, map);
//        System.out.println(request);


//        String url = URLConstant.MOVIE_INFO;
//        long id = 19938222;
//        url = url.replaceAll(":id", String.valueOf(id)) + "?apikey=" + "02646d3fb69a52ff072d47bf23cef8fd";
//        System.out.println(DoubanHttpUtils.getRequest(url));

//        ApplicationHome home = new ApplicationHome(getClass());
////        File jarFile = home.getSource();
//        System.out.println(jarFile.getParentFile().toString());

        List<MovieInfo> movieInfosFromPath = fileService.getMovieInfosFromPath("E:\\movies");
        String searchApikey = "0dad551ec0f84ed02907ff5c42e8ec70";
//        String searchApikey = "02646d3fb69a52ff072d47bf23cef8fd";
        String idApikey = "0b2bdeda43b5688921839c8ecb20399b";
//        String idApikey = "0df993c66c0c636e29ecbb5344252a4a";
        long startTime=System.currentTimeMillis();   //获取开始时间
        BaseResult doubanMovieInfos = douBanRequestService.getDoubanMovieInfos(movieInfosFromPath, searchApikey, idApikey);
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println(JSON.toJSONString(doubanMovieInfos));

        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");


//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, Object> map = new HashMap<>();
//        map.put("q", "庆余年 第一季");
//        map.put("apikey", "0dad551ec0f84ed02907ff5c42e8ec70");
//        System.out.println(restTemplate.getForObject(URLConstant.MOVIE_SEARCH+"?q={q}&apikey={apikey}", JSONObject.class, map));
////        JSONObject forObject = restTemplate.getForObject("https://api.douban.com/v2/movie/subject/5112751?apikey=0df993c66c0c636e29ecbb5344252a4a", JSONObject.class);
////        System.out.println(forObject);
    }


}
