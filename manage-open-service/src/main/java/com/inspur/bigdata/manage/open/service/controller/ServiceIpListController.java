package com.inspur.bigdata.manage.open.service.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.inspur.bigdata.manage.open.service.data.IpList;
import com.inspur.bigdata.manage.open.service.data.ServiceApply;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.service.*;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.inspur.bigdata.manage.open.service.util.OpenServiceConstants.getUserId;
import static org.loushang.framework.util.DateUtil.getCurrentTime2;

@CrossOrigin
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

    @Autowired
    private IServiceMonitorService monitorService;

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

    /**
     * 定时统计 监控表，将近十分钟频繁调用失败的ip拉黑
     */
    @PostConstruct
    public void ipBlackAutoHandler(){

        // 十分钟执行一次
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    System.out.println("开始执行：ipBlackAutoHandler");
                    Calendar beforeTime = Calendar.getInstance();
                    beforeTime.add(Calendar.MINUTE, -10);// 5分钟之前的时间
                    Date beforeD = beforeTime.getTime();
                    String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeD);
                    String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("result","200");
                    paramsMap.put("startTime", startTime);
                    paramsMap.put("endTime", endTime);
                    List<Map<String,Object>> ipList = monitorService.queryNotSuccessNearby(paramsMap);

                    for (Map<String,Object> item : ipList){
                        String ipString = (String)item.get("caller_ip");
                        Long count = (Long) item.get("count");
                        if (count>100) {
                            System.out.println("拉黑："+ipString+", 近5min调用失败次数为："+count+" 次");
                            // 拉黑
                            IpList ip = new IpList();
                            ip.setActive("true");
                            ip.setId(UUID.randomUUID().toString());
                            ip.setCreateTime(endTime);
                            ip.setProvider("system");
                            ip.setIpV4(ipString);
                            ip.setIpV6(ipString);
                            ip.setType("black");
                            try{
                                serviceIpListService.addIpList(ip);
                            }catch (Exception e){
                                System.out.println(e.toString());
                            }
                        }
                    }
                }catch (Exception e){
                    System.out.println("执行ipBlackAutoHandler异常："+e.toString());
                }
            }
        },0,10 * 60 *1000);


    }
}

