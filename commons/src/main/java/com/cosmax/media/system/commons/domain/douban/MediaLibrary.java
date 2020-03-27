package com.cosmax.media.system.commons.domain.douban;

import java.io.Serializable;
import java.util.Objects;

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
public class MediaLibrary implements Serializable {

    private static final long serialVersionUID = 7475103384046570679L;

    /**
     * 媒体库id
     */
    @TableId
    private Long id;

    /**
     * 媒体库名称
     */
    private String name;

    /**
     * 目录
     */
    private String directory;

    /**
     * 类型：0为电影，1为电视剧
     */
    private Integer type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaLibrary)) return false;
        MediaLibrary that = (MediaLibrary) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(directory, that.directory) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, directory, type);
    }
}
