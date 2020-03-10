package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageContrillerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageContrillerApi {
    @Autowired
    private PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findPage(@PathVariable("page") int pageNum,
                                        @PathVariable("size") int pageSize, QueryPageRequest queryPageRequest) {


        return pageService.findPage(pageNum,pageSize,queryPageRequest);
    }
}
