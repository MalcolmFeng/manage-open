package com.inspur.bigdata.manage.utils;

import com.alibaba.fastjson.JSON;
import com.inspur.bigdata.manage.utils.NifiKonxUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Log log = LogFactory.getLog(HttpUtil.class);
    public static final String USERNAME = "nifiadmin";
    private static final String PASSWORD = "175d6b94-8dd9-40e9-ac7b-5df7dd55e8d9";
    private static Map<String, String> tokenMap = new HashMap<>();
    private static Map<String, Long> expiresinMap = new HashMap<>();


    public static Map<String, String> getToken(String realmname) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", getCookie(realmname,false));
        headers.put("Accept", "application/json,application/xml,*/*");
        headers.put("Content-Type", "application/json");
        headers.put("X-Requested-With", "XMLHttpRequest");
        return headers;
    }
    public static Map<String, String> getTokenNoCache(String realmname) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", getCookie(realmname,true));
        headers.put("Accept", "application/json,application/xml,*/*");
        headers.put("Content-Type", "application/json");
        headers.put("X-Requested-With", "XMLHttpRequest");
        return headers;
    }

    public static synchronized String getCookie(String realmname,boolean noCache) {
        String KnoxHost = NifiKonxUtil.getKnoxHost(realmname).get("KnoxUrl");
        if (noCache || !expiresinMap.containsKey(realmname) || expiresinMap.get(realmname) - System.currentTimeMillis() < 200000L) {
            String url = "http://"+ KnoxHost +"/gateway/default/knoxtoken/api/v1/token";
            String np = USERNAME + "-" + realmname + ":" + PASSWORD;
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String(np.getBytes()));
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            System.out.println(execPost(url, headers, ""));
            JSONObject access = JSONObject.fromObject(execPost(url, headers, ""));
            
            expiresinMap.put(realmname, Long.valueOf(access.getString("expires_in")));
            tokenMap.put(realmname, realmname + "-jwt=" + access.getString("access_token"));
        }
        return tokenMap.get(realmname);
    }

    /**
     * GET方法访问URL
     *
     * @param url
     * @param headers
     * @return
     */
    public static String execGet(String url, Map<String, String> headers) {
        String str = "{error:404}";
        HttpGet httpGet = new HttpGet(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpGet.addHeader(key, headers.get(key));
            }
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("GET method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    /**
     * GET方法访问URL
     *
     * @param url
     * @return
     */
    public static String execGet(String authorization, String url) {
        String str = "{error:404}";
        HttpGet httpGet = new HttpGet(url);
        if (StringUtil.isNotEmpty(authorization)) {
            httpGet.addHeader("Authorization", authorization);
        }
        try (final CloseableHttpClient httpclient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("GET method failed to access URL，URL：" + url, e);
        }
        return str;
    }


    /**
     * POST方法访问URL
     *
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    public static String execPost(String url, Map<String, String> headers, String parameters) {
        String str = "{error:404}";
        HttpPost httpPost = new HttpPost(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        if (StringUtil.isNotEmpty(parameters)) {
            httpPost.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("POST method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    /**
     * POST方法访问URL
     *
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    public static Map<String, Object> execPost(String url, Map<String, String> headers, Map<String, String> parameters, String inputnmae, File file) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "404");
        HttpPost httpPost = new HttpPost(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addPart(inputnmae, new FileBody(file));
        if (StringUtil.isNotEmpty(parameters)) {
            for (String key : parameters.keySet()) {
                builder.addTextBody(key, parameters.get(key));
            }
        }
        httpPost.setEntity(builder.build());
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            String str = EntityUtils.toString(resEntity, "utf-8");
            map.put("code", "" + statusCode);
            map.put("message", str);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("POST method failed to access URL，URL：" + url, e);
        }
        return map;
    }

    /**
     * POST方法访问URL
     *
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    public static String execPost(String url, Map<String, String> headers, Map<String, String> parameters) {
        String str = "{error:404}";
        HttpPost httpPost = new HttpPost(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        if (StringUtil.isNotEmpty(parameters)) {
            List<NameValuePair> nvps = new ArrayList<>();
            for (String key : parameters.keySet()) {
                nvps.add(new BasicNameValuePair(key, parameters.get(key)));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            } catch (Exception e) {
            }
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("POST method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    /**
     * PUT方法访问URL
     *
     * @param url
     * @param headers
     * @param parameters
     * @return
     */
    public static String execPut(String url, Map<String, String> headers, String parameters) {
        String str = "{error:404}";
        HttpPut httpPut = new HttpPut(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpPut.addHeader(key, headers.get(key));
            }
        }
        if (StringUtil.isNotEmpty(parameters)) {
            httpPut.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpPut)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("PUT method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    /**
     * DELETE方法访问URL
     *
     * @param url
     * @param headers
     * @return
     */
    public static String execDelete(String url, Map<String, String> headers) {
        String str = "{error:404}";
        HttpDelete httpDelete = new HttpDelete(url);
        if (StringUtil.isNotEmpty(headers)) {
            for (String key : headers.keySet()) {
                httpDelete.addHeader(key, headers.get(key));
            }
        }
        try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
             final CloseableHttpResponse response = httpclient.execute(httpDelete)) {
            HttpEntity entity = response.getEntity();
            str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("DELETE method failed to access URL，URL：" + url, e);
        }
        return str;
    }

    
	/**
	 * POST方法访问URL
	 *
	 * @param url
	 * @param headers
	 * @param parameters
	 * @return
	 */
	public static String execPostContent(String url, Map<String, String> headers, Object content) {
		String str = "{error:500}";
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");

		if (!StringUtils.isEmpty(headers)) {
			for (String key : headers.keySet()) {
				httpPost.addHeader(key, headers.get(key));
			}
		}
		
		String jsonstr = JSON.toJSONString(content);
		
		StringEntity se = new StringEntity(jsonstr, "UTF-8");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

		RequestConfig requestConfig = RequestConfig.custom()    
		        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)    
		        .setSocketTimeout(5000).build();    
		httpPost.setConfig(requestConfig); 
		
		httpPost.setEntity(se);
		

		try (final CloseableHttpClient httpclient = createCloseableHttpClient(url);
				final CloseableHttpResponse response = httpclient.execute(httpPost)) {
			HttpEntity entity = response.getEntity();
			str = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			Map<String,String> result = new HashMap<String,String>();

			result.put("resultMessage", e.toString());
			result.put("resultCode", "500");
			result.put("error", "500");
			str = JSON.toJSONString(result);
		}
		return str;
	}

    public static CloseableHttpClient createCloseableHttpClient(String url) throws Exception {
        if (!url.toLowerCase().startsWith("https")) {
            return HttpClients.createDefault();
        }
        SSLContext sslcontext = createIgnoreVerifySSL();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(100);
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(connManager)
                .build();
    }

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
