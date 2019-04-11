/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.inspur.bigdata.manage.open.service.util.signconstants;

/**
 * 通用常量
 */
public class Constants {
    //签名算法HmacSha256
    public static final String HMAC_SHA256 = "HmacSHA256";
    //编码UTF-8
    public static final String ENCODING = "UTF-8";
    //UserAgent
    public static final String USER_AGENT = "demo/aliyun/java";
    //换行符
    public static final String LF = "\n";
    //串联符
    public static final String SPE1 = ",";
    //示意符
    public static final String SPE2 = ":";
    //连接符
    public static final String SPE3 = "&";
    //赋值符
    public static final String SPE4 = "=";
    //问号符
    public static final String SPE5 = "?";
    //默认请求超时时间,单位毫秒
    public static final int DEFAULT_TIMEOUT = 1000;
    //参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
    public static final String CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "X-Ca-";
    //contentTYpe
    //表单类型Content-Type
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";
    // 流类型Content-Type
    public static final String CONTENT_TYPE_STREAM = "application/octet-stream; charset=UTF-8";
    //JSON类型Content-Type
    public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    //XML类型Content-Type
    public static final String CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    //文本类型Content-Type
    public static final String CONTENT_TYPE_TEXT = "application/text; charset=UTF-8";
    //HttpHeader
    //请求Header Accept
    public static final String HTTP_HEADER_ACCEPT = "Accept";
    //请求Body内容MD5 Header
    public static final String HTTP_HEADER_CONTENT_MD5 = "Content-MD5";
    //请求Header Content-Type
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    //请求Header UserAgent
    public static final String HTTP_HEADER_USER_AGENT = "User-Agent";
    //请求Header Date
    public static final String HTTP_HEADER_DATE = "Date";
    //HttpMethod
    //GET
    public static final String GET = "GET";
    //POST
    public static final String POST = "POST";
    //PUT
    public static final String PUT = "PUT";
    //DELETE
    public static final String DELETE = "DELETE";
    //HttpSchema
    //HTTP
    public static final String HTTP = "http://";
    //HTTPS
    public static final String HTTPS = "https://";
    //Method
    //SignUtil
}
