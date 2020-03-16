package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;



    public QueryResponseResult findTemplateBySiteId(String siteId){
        if(StringUtils.isNotEmpty(siteId)) {
            List<CmsTemplate> cmsTemplateList = cmsTemplateRepository.findCmsTemplateBySiteId(siteId);
            QueryResult queryResult = new QueryResult();
            queryResult.setList(cmsTemplateList);
            queryResult.setTotal(cmsTemplateList.size());
            return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        }else {
            return new QueryResponseResult(CommonCode.CHOOSESITR,null);
        }
    }
}
