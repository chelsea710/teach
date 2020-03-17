package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findPage(int page, int size, QueryPageRequest queryPageRequest){
        if(ObjectUtils.isEmpty(queryPageRequest)){
            queryPageRequest = new QueryPageRequest();
        }

        CmsPage cmsPage = new CmsPage();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        if(page < 0){
            page = 1;
        }
        if(size < 0){
            size = 10;
        }
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        Pageable pageable = PageRequest.of((page-1),size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult result = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return result;
    }


    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cmsPageSelect = cmsPageRepository.findCmsPageBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (!ObjectUtils.isEmpty(cmsPageSelect)) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);

    }

    public CmsPage findById(String id) {
        return cmsPageRepository.findById(id).orElse(null);
    }


    public CmsPageResult edit(String id, CmsPage cmsPage) {
        CmsPage cmsPage1 = this.findById(id);
        if (ObjectUtils.isEmpty(cmsPage1)) {
            ExceptionCast.cast(CmsCode.CMS_COURSE_PERVIEWISNULL);
        }
        CmsPage one = new CmsPage();
        one.setTemplateId(cmsPage.getTemplateId());
        //更新所属站点
        one.setSiteId(cmsPage.getSiteId());
        //更新页面别名
        one.setPageAliase(cmsPage.getPageAliase());
        //更新页面名称
        one.setPageName(cmsPage.getPageName());
        //更新访问路径
        one.setPageWebPath(cmsPage.getPageWebPath());
        //更新物理路径
        one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        one.setPageId(id);
        //执行更新
        CmsPage save = cmsPageRepository.save(one);
        return new CmsPageResult(CommonCode.SUCCESS, save);

    }

    public ResponseResult delete(String id) {
        CmsPage cmsPage = this.findById(id);
        if(!ObjectUtils.isEmpty(cmsPage)){
            cmsPageRepository.deleteById(id);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
