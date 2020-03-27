package com.cosmax.media.system.business.dto;

import lombok.Data;

/**
 * @program: media-system
 * @description: 媒体库参数dto
 * @author: Cosmax
 * @create: 2020/03/01 10:12
 */

@Data
public class MediaLibraryDto {

    /**
     * 媒体库id
     */
    private Long id;
    /**
     * 媒体库目录
     */
    private String directory;
}
