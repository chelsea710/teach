package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePerviewController extends BaseController {

    @Autowired
    private PageService pageService;

    //页面预览
    @RequestMapping(value = "/cms/preview/{pageId}",method = RequestMethod.GET)
    public void preivew(@PathVariable("pageId") String pageId) throws IOException {
        String pathHtml = pageService.getPathHtml(pageId);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pathHtml.getBytes("utf-8"));
    }
}
