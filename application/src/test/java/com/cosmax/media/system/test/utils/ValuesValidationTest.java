package com.cosmax.media.system.test.utils;

import com.alibaba.fastjson.JSON;
import com.cosmax.media.system.MediaSystemApplication;
import com.cosmax.media.system.commons.BaseResult;
import com.cosmax.media.system.commons.domain.douban.MediaLibrary;
import com.cosmax.media.system.commons.validation.ValuesValidation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: media-system
 * @description: 校验测试
 * @author: Cosmax
 * @create: 2020/03/01 09:23
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediaSystemApplication.class)
public class ValuesValidationTest {

    @Test
    public void test(){
        MediaLibrary mediaLibrary = new MediaLibrary();
        mediaLibrary.setId(1L);
        mediaLibrary.setName("233");
//        mediaLibrary.setDirectory();
//        mediaLibrary.setType();
//        mediaLibrary.setIsSearch(1);

        mediaLibrary.setDirectory("233333");
        mediaLibrary.setType(0);
        ValuesValidation<MediaLibrary> mediaLibraryValuesValidation = new ValuesValidation<>();
        BaseResult baseResult = mediaLibraryValuesValidation.checkIsNull(mediaLibrary, "id", "name", "directory", "type", "isSearch");
        System.out.println(JSON.toJSONString(baseResult));

    }
}
