package com.inspur.bigdata.manage.open.service.pay.controller;

import com.inspur.bigdata.manage.open.service.pay.data.PayAccountCapital;
import com.inspur.bigdata.manage.open.service.pay.service.IPayService;
import com.inspur.bigdata.manage.open.service.pay.util.UUIDTool;
import com.inspur.bigdata.manage.utils.OpenDataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IPayService payService;

    @RequestMapping(value = "/requestForPay", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Map requestForPay(HttpServletRequest request, HttpServletResponse httpResponse, @RequestParam Map<String, String> param)
            throws ServletException, IOException {

        Map<String, Object> retMap = new HashMap<String, Object>();

        // 如果订单id为空，生成一个订单ID(或者说，是不是直接在这里生成订单id就好)
        String orderId = param.get("orderId");
        if (orderId == null || "".equals(orderId)) {
            orderId = UUIDTool.getPrefixRandomWithTime("OPENSC", 6);
            param.put("orderId", orderId);
        }

        param.put("userId", OpenDataConstants.getUserId());

        Map map = payService.requestForPay(param);
        //retMap.put("data", map);
        String code = (String) map.get("code");
        String data = (String) map.get("data");
        if ("200".equals(code)) {
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(data);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
            return null;
        }
        return map;
    }

    /**
     * 账户总览页面
     *
     * @return
     */
    @RequestMapping(value = "/accountOverView", method = RequestMethod.GET)
    @ResponseBody
    public PayAccountCapital getAccountOverView(@RequestParam Map<String, String> param) {
        // 查询账户余额
        String userId;
        if (param.get("userId") != null) {
            userId = param.get("userId");
        } else {
            userId = OpenDataConstants.getUserId();
        }
        return payService.getPayAccountByUserId(userId);
    }

    @RequestMapping("/payNotifyUrl")
    @ResponseBody
    public String payNotifyUrl(HttpServletRequest request, @RequestParam Map<String, String> param) {

        Map<String, Object> retMap = new HashMap<String, Object>();

        Map map = payService.payNotifyUrl(request, param);

        // https://docs.open.alipay.com/270/105902/
        // 程序执行完后必须打印输出“success”（不包含引号）。
        return "success";
    }
}
