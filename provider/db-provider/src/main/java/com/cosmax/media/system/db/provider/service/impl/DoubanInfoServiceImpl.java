package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.DoubanAllInfo;
import com.cosmax.media.system.commons.domain.douban.DoubanInfo;
import com.cosmax.media.system.commons.response.DoubanResponse;
import com.cosmax.media.system.db.provider.mapper.DoubanInfoMapper;
import com.cosmax.media.system.db.api.DoubanInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Service
public class DoubanInfoServiceImpl extends ServiceImpl<DoubanInfoMapper, DoubanInfo> implements DoubanInfoService {

    @Resource
    private DoubanInfoMapper doubanInfoMapper;

    @Override
    public DoubanAllInfo getDoubanInfosByLibraryId(Long id) {
        return doubanInfoMapper.getDoubanInfosByLibraryId(id);
    }

    @Override
    public DoubanResponse getDoubanResponseByVideosId(Long id) {
        return doubanInfoMapper.getDoubanResponseByVideosId(id);
    }
}
