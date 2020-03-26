package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.config.RabbitmqConfig;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听mq
 */
@Component
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) throws Exception {
        Map map = JSON.parseObject(msg,Map.class);
        String pageId = map.get("pageId").toString();
        cmsPageRepository.findById(pageId).orElseThrow(()->new CustomException(CommonCode.FAIL));
        pageService.savePageToServerPath(pageId);
    }

}
