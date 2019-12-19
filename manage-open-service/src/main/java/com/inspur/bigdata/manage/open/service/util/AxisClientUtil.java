package com.inspur.bigdata.manage.open.service.util;

import com.inspur.bigdata.manage.open.service.data.ApiServiceMonitor;
import com.inspur.bigdata.manage.open.service.data.ServiceDef;
import com.inspur.bigdata.manage.open.service.data.ServiceInput;
import net.sf.json.JSONObject;
import org.apache.axis.AxisEngine;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.soap.SOAPConstants;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AxisClientUtil {


    public static String executeRPCAxisClient(boolean success, ServiceDef ws, List<ServiceInput> serviceInputList, ApiServiceMonitor apiServiceMonitor) throws Exception {
        JSONObject serviceInputParam = new JSONObject();
        Object[] parameters = new Object[serviceInputList.size()];
        /*
        命名空间
         NameSpace为axis中的SOAPActionURI
        方法名
         Sc_ws_function为axis中的OperationNameQName，
         其中localPart对应OperationDesc中的name，
         字符串格式是{namespaceURI}localPart或者localPart，是QName格式
         */
        Call call = initAxisClientCall(ws.getScAddr(), ws.getNameSpace(), ws.getSc_ws_function(), serviceInputList, parameters, serviceInputParam);
        apiServiceMonitor.setServiceInput(serviceInputParam.toString());
        String resp = (String) call.invoke(parameters);
        return resp;
    }

    /**
     * 初始化Call
     */
    private static Call initAxisClientCall(String address, String SOAPActionURI, String operationName, List<ServiceInput> serviceInputList,
                                           Object[] parameters, JSONObject serviceInputParam) throws ServiceException, MalformedURLException {
        Service service = new Service();
        QName opName = QName.valueOf(operationName);
        Call call = null;
        call = (Call) service.createCall();
        call.setTargetEndpointAddress(new URL(address));
        call.setOperation(initAxisClientOperation(opName.getLocalPart(), serviceInputList, parameters, serviceInputParam));
        call.setUseSOAPAction(true);
        call.setSOAPActionURI(SOAPActionURI == null ? "" : SOAPActionURI);
        call.setEncodingStyle(null);
        call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
        call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(opName);
        return call;
    }

    /**
     * 初始化服务描述
     */
    private static OperationDesc initAxisClientOperation(String name, List<ServiceInput> serviceInputList,
                                                         Object[] parameters, JSONObject serviceInputParam) {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName(name);
        int j = 0;
        /*
        后端参数描述
            ScDescription对应的是returnTypeQName，字符串格式是{namespaceURI}localPart或者localPart，是QName格式
        参数描述
            Description对应的是returnQName，字符串格式是{namespaceURI}localPart或者localPart，是QName格式
        后端参数名称
            ScName对应的是ParameterDesc中的QName，字符串格式是{namespaceURI}localPart或者localPart，是QName格式
         */
        String returnTypeStr = "";
        String returnQNameStr = "";
        //取出所有的入参并赋值
        for (ServiceInput in : serviceInputList) {
            if (j == 0) {
                returnTypeStr = in.getScDescription();
                returnQNameStr = in.getDescription();
            }
            QName pd = QName.valueOf(in.getScName());
            param = new ParameterDesc(pd,
                    ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
            param.setOmittable(true);
            oper.addParameter(param);
            parameters[j++] = in.getValue();
            serviceInputParam.put(pd.getLocalPart(), in.getValue());
        }
        //暂时只支持string格式的返回值
        oper.setReturnType(QName.valueOf(returnTypeStr));
        oper.setReturnClass(String.class);
        oper.setReturnQName(QName.valueOf(returnQNameStr));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        return oper;
    }
}
