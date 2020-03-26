package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        one.setDataUrl(cmsPage.getDataUrl());
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


    /**
     * 根据页面Id 执行静态化程序 获取html文件字符串
     * @return html文件字符串
     */
    public String getPathHtml(String pageId){
        // 1:获得 页面对象
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        if(!cmsPageOptional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cmsPage = cmsPageOptional.get();
        // 2:通过dataUrl 获取model
        Map model = this.getMapData(cmsPage);
        if(ObjectUtils.isEmpty(model)){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 3:通过templateId  获得fileId 并通过gridFs 获得模板html String
        String templateContent = this.getTemplateString(cmsPage);
        // 4:执行静态化返回html 字符串
        return this.generateHtml(templateContent,model);
    }

    /**
     * 执行页面静态化 获得 静态化后的页面 字符串文件
     * @param templateContent 模板文件HtmlString
     * @param model 模型对象
     * @return 最终结果
     */
    private String generateHtml(String templateContent, Map model) {
        try {

            Configuration configuration = new Configuration(Configuration.getVersion());
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("templateString", templateContent);

            configuration.setTemplateLoader(stringTemplateLoader);

            Template template = configuration.getTemplate("templateString", "utf-8");

            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        }catch (Exception e){
            ExceptionCast.cast(CommonCode.FAIL);
        }
       return null;
    }


    /**
     * 执行静态化所需要的 模板文件htmlString
     * @param cmsPage 页面对象
     * @return 模板html 字符串
     */
    private String getTemplateString(CmsPage cmsPage) {
        Optional<String> templateId = Optional.ofNullable(cmsPage.getTemplateId());
        if(!templateId.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String s = templateId.get();
        Optional<CmsTemplate> cmsTemplateOptional = cmsTemplateRepository.findById(s);
        if(!cmsTemplateOptional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsTemplate cmsTemplate = cmsTemplateOptional.get();
        String fileId = Optional.ofNullable(cmsTemplate.getTemplateFileId()).orElseThrow(() -> new CustomException(CommonCode.FAIL));

        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile,stream);

        try {
            String result = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return Optional.ofNullable(result).orElseThrow(() -> new CustomException(CommonCode.FAIL));
        } catch (IOException e) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return null;
    }

    /**
     * 获得静态化所需要的模型数据
     * @param cmsPage 页面对象
     * @return 模型数据
     */
    private Map getMapData(CmsPage cmsPage) {
        Optional<String> dataUrl = Optional.ofNullable(cmsPage.getDataUrl());
        if(!dataUrl.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String s = dataUrl.get();
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(s, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 发布页面
     * @param pageId 页面id
     * @return 是否成功
     */
    public ResponseResult post(String pageId) throws Exception{
        CmsPage cmsPage = cmsPageRepository.findById(pageId).orElseThrow(() -> new CustomException(CommonCode.FAIL));
        //1:执行页面静态化
        String htmlContent = this.getPathHtml(pageId);
        //2:将静态化的文件存入grisFs中
        this.saveGridFs(htmlContent,cmsPage);
        //3:发消息
        this.sendMessageByMq(cmsPage);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 发消息
     * @param cmsPage 页面对象
     */
    private void sendMessageByMq(CmsPage cmsPage) {
        Map<String,String> msg = new HashMap<>();
        msg.put("pageId",cmsPage.getPageId());
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,cmsPage.getSiteId(), JSON.toJSONString(msg));
    }


    private void saveGridFs(String htmlContent, CmsPage cmsPage) throws IOException {
        InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
        /**
         * 保存之前把之前的文件删除掉
         */
        if(StringUtils.isNotEmpty(cmsPage.getHtmlFileId())) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(cmsPage.getHtmlFileId())));
        }
        ObjectId htmlFileId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        cmsPage.setHtmlFileId(htmlFileId.toHexString());
        cmsPageRepository.save(cmsPage);
    }
}
