package com.xmsmartcity.maintain.controller.web;

import com.xmsmartcity.maintain.service.CommonService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Owner on 2017/11/21 0021.
 */
@Controller
@RequestMapping(value = "/api/common")
public class WebCommonController {

    @Autowired
    private CommonService service;

    /**
     * 全局搜索
     * @param searchContent
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/search/overall")
    @ResponseBody
    public String searchContent(Integer userId,String searchContent,Integer type,Integer pageNum,Integer pageSize){
        Assert.state(StringUtils.isNotEmpty(searchContent), Constant.PARAMS_ERROR+"：searchContent错误,为null或为空");
        Assert.state(pageNum > 0,Constant.PARAMS_ERROR+" pageNum参数错误");
        Assert.state(pageSize > 0,Constant.PARAMS_ERROR+" pageSize参数错误");
        Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
        Object result = service.searchOverallData(userId,searchContent,type,pageNum,pageSize);
        return Utils.toSuccessJsonResults(result);
    }
}
