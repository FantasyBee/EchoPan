package com.echopan.service.impl;

import com.echopan.entity.constants.Constants;
import com.echopan.entity.dto.SessionShareDto;
import com.echopan.entity.enums.PageSize;
import com.echopan.entity.enums.ResponseCodeEnum;
import com.echopan.entity.enums.ShareValidTypeEnums;
import com.echopan.entity.po.FileShare;
import com.echopan.entity.query.FileShareQuery;
import com.echopan.entity.query.SimplePage;
import com.echopan.entity.vo.PaginationResultVO;
import com.echopan.exception.BusinessException;
import com.echopan.mappers.FileShareMapper;
import com.echopan.service.FileShareService;
import com.echopan.utils.DateUtil;
import com.echopan.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 分享信息 业务接口实现
 */
@Service("fileShareService")
public class FileShareServiceImpl implements FileShareService {

    @Resource
    private FileShareMapper<FileShare, FileShareQuery> fileShareMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<FileShare> findListByParam(FileShareQuery param) {
        return this.fileShareMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(FileShareQuery param) {
        return this.fileShareMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<FileShare> findListByPage(FileShareQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<FileShare> list = this.findListByParam(param);
        PaginationResultVO<FileShare> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(FileShare bean) {
        return this.fileShareMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<FileShare> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileShareMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<FileShare> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileShareMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据ShareId获取对象
     */
    @Override
    public FileShare getFileShareByShareId(String shareId) {
        return this.fileShareMapper.selectByShareId(shareId);
    }

    /**
     * 根据ShareId修改
     */
    @Override
    public Integer updateFileShareByShareId(FileShare bean, String shareId) {
        return this.fileShareMapper.updateByShareId(bean, shareId);
    }

    /**
     * 根据ShareId删除
     */
    @Override
    public Integer deleteFileShareByShareId(String shareId) {
        return this.fileShareMapper.deleteByShareId(shareId);
    }

    @Override
    public void saveShare(FileShare share) {
        ShareValidTypeEnums typeEnum = ShareValidTypeEnums.getByType(share.getValidType());
        if (null == typeEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (typeEnum != ShareValidTypeEnums.FOREVER) {
            share.setExpireTime(DateUtil.getAfterDate(typeEnum.getDays()));
        }
        Date curDate = new Date();
        share.setShareTime(curDate);
        if (StringTools.isEmpty(share.getCode())) {
            share.setCode(StringTools.getRandomString(Constants.LENGTH_5));
        }
        share.setShareId(StringTools.getRandomString(Constants.LENGTH_20));
        this.fileShareMapper.insert(share);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileShareBatch(String[] shareIdArray, String userId) {
        Integer count = this.fileShareMapper.deleteFileShareBatch(shareIdArray, userId);
        if (count != shareIdArray.length) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    @Override
    public SessionShareDto checkShareCode(String shareId, String code) {
        FileShare share = this.fileShareMapper.selectByShareId(shareId);
        if (null == share || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
            throw new BusinessException(ResponseCodeEnum.CODE_902);
        }
        if (!share.getCode().equals(code)) {
            throw new BusinessException("提取码错误");
        }

        //更新浏览次数
        this.fileShareMapper.updateShareShowCount(shareId);
        SessionShareDto shareSessionDto = new SessionShareDto();
        shareSessionDto.setShareId(shareId);
        shareSessionDto.setShareUserId(share.getUserId());
        shareSessionDto.setFileId(share.getFileId());
        shareSessionDto.setExpireTime(share.getExpireTime());
        return shareSessionDto;
    }
}