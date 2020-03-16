package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;


    public QueryResponseResult findSiteAll() {
        QueryResult<CmsSite> queryResult = new QueryResult<>();
        List<CmsSite> siteList = cmsSiteRepository.findAll();
        queryResult.setTotal(siteList.size());
        queryResult.setList(siteList);
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
