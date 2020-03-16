package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


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


    public CmsPageResult add(CmsPage cmsPage){
        CmsPage cmsPageSelect = cmsPageRepository.findCmsPageBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if(ObjectUtils.isEmpty(cmsPageSelect)){
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        } else {
            return new CmsPageResult(CommonCode.FAIL,null);
        }
    }
}
