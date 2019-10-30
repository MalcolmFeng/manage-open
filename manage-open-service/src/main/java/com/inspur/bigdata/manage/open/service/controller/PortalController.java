package com.inspur.bigdata.manage.open.service.controller;

import com.inspur.bigdata.manage.common.utils.PropertiesUtil;
import com.inspur.bigdata.manage.open.service.data.ServiceGroup;
import com.inspur.bigdata.manage.open.service.util.OpenServiceConstants;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songlili on 2019/3/12.
 */
@CrossOrigin
@Controller
@RequestMapping("/open/portal")
public class PortalController {

    @RequestMapping(value = "/getPortalPage", method = RequestMethod.GET)
    public ModelAndView getPortalPage() {
        Map<String, Object> model = new HashMap<String, Object>();
        String url = PropertiesUtil.getValue(OpenDataConstants.CONF_PROPERTIES, "od.domain");
        String realm=OpenServiceConstants.getRealm();
        Map<String,Object> param=new HashMap<String,Object>();
        model.put("dataurl",url);
        model.put("realm",realm);
        return new ModelAndView("portal/index",model);
    }

}
