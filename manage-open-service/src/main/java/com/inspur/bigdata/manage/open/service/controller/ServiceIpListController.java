package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.data.IpList;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.service.IAppManage;
import com.inspur.bigdata.manage.open.service.service.IServiceApplyService;
import com.inspur.bigdata.manage.open.service.service.IServiceDefService;
import com.inspur.bigdata.manage.open.service.service.IServiceIpListService;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.DateUtil;
import org.loushang.framework.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inspur.bigdata.manage.open.service.util.OpenServiceConstants.getUserId;
import static org.loushang.framework.util.DateUtil.getCurrentTime2;

@Controller
@RequestMapping("/open/iplist")
public class ServiceIpListController {
    private static Log log = LogFactory.getLog(ServiceIpListController.class);

    @Autowired
    private IServiceApplyService serviceApplyService;

    @Autowired
    private IServiceIpListService serviceIpListService;

    @Autowired
    private IServiceDefService serviceDefService;

    @Autowired
    private IAppManage appManage;

    /**
     * 我的申请列表页
     *
     * @return
     */
    @RequestMapping({"/getPage"})
    public String toApplyListIndex() {
        return "service/iplist/iplist";
    }

    /**
     * 跳转创建页面
     *
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public ModelAndView create(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("edit", false);
        model.put("IpList", new ServiceDef());
        return new ModelAndView("service/iplist/createiplist", model);
    }

    //更新黑白名单
    @RequestMapping(value = "/updateIpList", method = RequestMethod.POST)
    @ResponseBody
    public void updateIpList(@RequestBody IpList ipList) {
        serviceIpListService.updateIpList(ipList);
    }

    //添加黑白名单
    @RequestMapping(value = "/addIpList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addIpList(@RequestBody IpList ipList) {
        JSONObject result = new JSONObject();
        if (StringUtil.isEmpty(ipList.getIpV4()) && StringUtil.isEmpty(ipList.getIpV6())) {
            result.put("code", "500");
            result.put("error", "IP地址不存在!");
        } else if (StringUtil.isEmpty(ipList.getType()) || (!ipList.getType().equals("black") && !ipList.getType().equals("white"))) {
            result.put("code", "500");
            result.put("error", "黑白名单类型不正确!");
        } else if (StringUtil.isEmpty(ipList.getActive()) || (!ipList.getActive().equals("true") && !ipList.getActive().equals("false"))) {
            result.put("code", "500");
            result.put("error", "是否生效类型不正确!");
        } else {
            try {
                ipList.setId(UUIDGenerator.getUUID());
                ipList.setProvider(getUserId());
                if (StringUtil.isEmpty(ipList.getCreateTime())) {
                    ipList.setCreateTime(getCurrentTime2());
                }
                serviceIpListService.addIpList(ipList);
                result.put("code", "200");
            } catch (Exception e) {
                log.warn(e.getMessage());
                result.put("code", "500");
                result.put("error", e.getMessage());
            }
        }
        return result;
    }

    //获取列表
    @RequestMapping(value = "/getIpBlacklist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAPIAuthList(@RequestBody Map<String, String> parameters) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("limit", parameters.get("limit"));
        param.put("start", parameters.get("start"));
        String userId = getUserId();
        if (StringUtil.isNotEmpty(parameters.get("ipV4"))) {
            param.put("ipV4", parameters.get("ipV4"));
        }
        if (StringUtil.isNotEmpty(parameters.get("ipV6"))) {
            param.put("ipV6", parameters.get("ipV6"));
        }
        if (StringUtil.isNotEmpty(parameters.get("type"))) {
            param.put("type", parameters.get("type"));
        }
        if (StringUtil.isNotEmpty(parameters.get("active"))) {
            param.put("active", parameters.get("active"));
        }
        if (StringUtil.isNotEmpty(parameters.get("provider"))) {
            param.put("provider", parameters.get("provider"));
        }
        if (StringUtil.isNotEmpty(parameters.get("startTime"))) {
            param.put("startTime", parameters.get("startTime"));
        }
        if (StringUtil.isNotEmpty(parameters.get("endTime"))) {
            param.put("endTime", parameters.get("endTime"));
        }
        List<IpList> list = serviceIpListService.getIpList(param);
        int total = PageUtil.getTotalCount();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }

    //删除黑白名单
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public boolean deleteApply(@PathVariable("id") String id) {
        boolean result = false;
        if (StringUtils.isNotEmpty(id)) {
            serviceIpListService.deleteIpListById(id);
            result = true;
        }
        return result;
    }
}

