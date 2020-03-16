package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 接口類
 * @author LHR
 * @date 20200310
 */
@Api(value = "查询站点",description = "查询站点")
public interface CmsSiteContrillerApi {

    /**
     * 查询站点
     * @return 统一返回格式
     */
    @ApiOperation("查询站点")
    public QueryResponseResult findSiteAll();
}
