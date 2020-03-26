package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {


    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 页面发布最后一步 从文件服务器中拿文件
     * 发布到制定位置
     * 文件路径 = 站点的物理路径 + 页面物理路径 + 页面名称
     * @param pageId
     */
    public void savePageToServerPath(String pageId) throws Exception{

        //1:根据页面Id 查询页面信息
        CmsPage cmsPage = this.findCmspageByPageId(pageId);
        //2:根据cmsPage 找出 文件个服务器中的 文件
        String htmlFileId = Optional.ofNullable(cmsPage.getHtmlFileId()).orElseThrow(()->new CustomException(CommonCode.FAIL));
        //3:去gridFs取出静态化好的文件
        InputStream inputStream = this.getHtmlFile(htmlFileId);
        //4:查询站点 寻找html文件路径并替换
        CmsSite cmsSite = cmsSiteRepository.findById(cmsPage.getSiteId()).orElseThrow(() -> new CustomException(CommonCode.FAIL));
        String pagePath = cmsPage.getPagePhysicalPath() +
                cmsPage.getPageName();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(pagePath));
        IOUtils.copy(inputStream,fileOutputStream);
        fileOutputStream.close();

    }

    private InputStream getHtmlFile(String htmlFileId) throws IOException {
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile,stream);
        InputStream inputStream = gridFsResource.getInputStream();
        return inputStream;
    }


    private CmsPage findCmspageByPageId(String pageId) {
        return cmsPageRepository.findById(pageId).orElseThrow(()->new CustomException(CommonCode.FAIL));
    }
}
