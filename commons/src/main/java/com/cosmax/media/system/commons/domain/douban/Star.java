package com.cosmax.media.system.commons.domain.douban;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Star implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 演员id
     */
    @TableId
    private Long starId;

    /**
     * 演员名
     */
    private String starName;

    /**
     * 演员照片位置
     */
    private String starImg;

    /**
     * 演员简单介绍
     */
    private String starBrief;


}
