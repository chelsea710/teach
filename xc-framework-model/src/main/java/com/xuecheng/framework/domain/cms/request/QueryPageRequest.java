package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * 查询页面的条件Dto
 * @author LHR
 * @date 20200310
 */
@Data
public class QueryPageRequest {

    /**
     * 站点Id
     */
    private String siteId;
    /**
     * 页面ID
     */
    private String pageId;
    /**
     * 页面名称
     */
    private String pageName;
    /**
     * 别名
     */
    private String pageAliase;
    /**
     * 模板Id
     */
    private String templateId;
}
