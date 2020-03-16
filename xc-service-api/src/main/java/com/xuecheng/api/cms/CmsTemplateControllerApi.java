package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = "查询模板",description = "查询模板")
public interface CmsTemplateControllerApi {
    /**
     * 查询站点
     * @return 统一返回格式
     */
    @ApiOperation("查询模板")
    public QueryResponseResult findTemplateBySiteId(String siteId);
}
