package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * 接口類
 * @author LHR
 * @date 20200310
 */
public interface CmsPageContrillerApi {

    /**
     * 分页查询页面信息
     * @param pageNum 页码
     * @param pageSize 页数
     * @param queryPageRequest 查询条件
     * @return 统一返回格式
     */
    public QueryResponseResult findPage(int pageNum, int pageSize, QueryPageRequest queryPageRequest);
}
