package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.open.service.data.DevGroup;
import com.inspur.bigdata.manage.open.service.data.ServiceGroup;
import com.inspur.bigdata.manage.open.service.service.IDevGroupService;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.mybatis.PageUtil;
import org.loushang.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/2/21.
 */
@Controller
@RequestMapping("/dev/group")
public class DevGroupController {
    private static final Log log= LogFactory.getLog(DevGroupController.class);

    @Autowired
    private IDevGroupService devGroupService;

    @RequestMapping({"/getPage"})
    public String toApiListIndex() {
        return "service/devgroup/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDevGroupList(@RequestBody Map<String, String> parameters)
   {
        Map<String, Object> param = new HashMap<String, Object>();
        String userId= OpenDataConstants.getUserId();
        //String userId="test11-realm1234";
        param.put("userId",userId);
        param.put("limit",parameters.get("limit"));
        param.put("start",parameters.get("start"));
        List<DevGroup> list = devGroupService.getGroupList(param);
        int total = PageUtil.getTotalCount();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("total", total);

        return result;
    }
    @RequestMapping(value = "/listNoPage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDevGroupListNopage(@RequestBody Map<String, String> parameters)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        String userId= OpenDataConstants.getUserId();
        param.put("userId",userId);
        List<DevGroup> list = devGroupService.getGroupList(param);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);

        return result;
    }
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, String> saveDevGroup(@RequestParam Map<String, String> param) {

        String userId= OpenDataConstants.getUserId();
        //String userId="test11-realm1234";
        Map<String,String> result=new HashMap<String,String>();
        String msg="保存失败";
        DevGroup group = new DevGroup();
        group.setId(param.get("id"));
        group.setName(param.get("name"));
        group.setContext(param.get("context"));
        group.setUserId(userId);
        group.setDescription(param.get("description"));

        boolean isRegisted = devGroupService.isRegisted(userId, group.getName());
        boolean isExistedContext = devGroupService.isExistedContext(group.getContext());


        try {
            if(!isRegisted) {   //无同名的
                if (group.getId() == null || "".equals(group.getId())) {
                    group.setCreate_time(DateUtil.getCurrentTime2());
                    devGroupService.insert(group);
                    result.put("msg", "保存api分组成功");
                } else {//编辑
                    if (!isExistedContext) { //名字context均修改
                        group.setUpdate_time(DateUtil.getCurrentTime2());
                        devGroupService.update(group);
                        result.put("msg", "更新api分组成功");
                    } else { //名字改但是context有重复
                        DevGroup dg = devGroupService.getById(param.get("id"));
                        if (dg.getContext().equals(param.get("context"))) { //名字改但是context和自身重复
                            group.setUpdate_time(DateUtil.getCurrentTime2());
                            devGroupService.update(group);
                            result.put("msg", "更新api分组成功");
                        } else {
                            result.put("msg", "context重复，请重新编写");
                            result.put("flag", "false");
                            return result;
                        }
                    }
                }
                result.put("flag","true");
                return result;
            } else {    //有同名
                if (group.getId() == null || "".equals(group.getId())) {
                    msg = "该用户已存在相同api分组!";
                    result.put("msg", msg);
                    result.put("flag","false");
                    return result;
                }
                DevGroup dg = devGroupService.getById(param.get("id"));
                if (!dg.getName().equals(param.get("name"))) {
                    result.put("msg", "已存在此分组");
                    result.put("flag", "false");
                    return result;
                } else {
                    System.out.println(dg.getContext());
                    System.out.println(param.get("context"));
                    if (dg.getContext()!=null&&!dg.getContext().equals(param.get("context")) && isExistedContext){
                        result.put("msg", "context重复，请重新编写");
                        result.put("flag", "false");
                        return result;
                    } else {
                        group.setUpdate_time(DateUtil.getCurrentTime2());
                        devGroupService.update(group);
                        result.put("msg", "更新api分组成功");
                        result.put("flag", "true");
                        return result;
                    }
                }


            }

        } catch (Exception e) {
            log.error("保存api分组出错.", e);
            return result;
        }
    }
    @RequestMapping("/edit/{id}")
    public ModelAndView editServiceGroupById(@PathVariable("id") String groupId) {
        DevGroup group = devGroupService.getById(groupId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("group", group);
        return new ModelAndView("/service/devgroup/edit", model);
    }
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public boolean deletedevGroupById(@PathVariable("id") String id) {
        try {
            devGroupService.deletebyId(id);
            return true;
        } catch (Exception e) {
            log.error("删除api分组出错.", e);
            return false;
        }
    }
}
