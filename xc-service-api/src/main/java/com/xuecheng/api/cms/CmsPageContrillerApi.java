package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 接口類
 * @author LHR
 * @date 20200310
 */
@Api(value = "cms页面管理接口",description = "cms页面管理接口,提供页面的增,删,改,查")
public interface CmsPageContrillerApi {

    /**
     * 分页查询页面信息
     * @param pageNum 页码
     * @param pageSize 页数
     * @param queryPageRequest 查询条件
     * @return 统一返回格式
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int"),
    })
    public QueryResponseResult findPage(int pageNum, int pageSize, QueryPageRequest queryPageRequest);



    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);


    @ApiOperation("根据Id查询页面")
    public CmsPage findById(String id);

    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("删除页面")
    public ResponseResult delete(String id);
}
