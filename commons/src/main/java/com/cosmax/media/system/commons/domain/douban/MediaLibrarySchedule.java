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
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MediaLibrarySchedule implements Serializable {


    private static final long serialVersionUID = 3537031139465742122L;
    /**
     * 媒体库id
     */
    @TableId
    private Long libraryId;

    /**
     * 状态：0为创建|更新，1为删除
     */
    private Integer state;

    /**
     * 0为未完成，1为完成
     */
    private Integer isSuccess;

    /**
     * 保存状态
     */
    public static final Integer SAVE_STATE = 0;

    /**
     * 删除状态
     */
    public static final Integer DELETE_STATE = 1;
}
