package com.cosmax.media.system.db.provider.service.impl;

import com.cosmax.media.system.commons.domain.douban.Star;
import com.cosmax.media.system.db.provider.mapper.StarMapper;
import com.cosmax.media.system.db.api.StarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cosmax
 * @since 2020-02-09
 */
@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {

}
