package com.cosmax.media.system.commons.domain.douban;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
public class VideoKind implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 类型id
     */
    @TableId(value = "kind_id", type = IdType.AUTO)
    private Integer kindId;

    /**
     * 类型名
     */
    private String kindName;


}
