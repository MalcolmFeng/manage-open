package com.inspur.bigdata.manage.open.cloud.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * rest请求工具类，封装HttpClient
 */
public class HttpRequestUtil {

	/**
	 * 模拟请求，请求类型为application/x-www-form-urlencoded
	 * 
	 * @param url
	 *            资源地址
	 * @param params
	 *            参数列表
	 * @param encoding
	 *            编码
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String doPostUrlEncoded(String url, Map<String, String> params, String encoding)
			throws ParseException, IOException {
		String body = "";

		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();

		return body;
	}

	/**
	 * 模拟请求，请求方法为put，请求类型为application/x-www-form-urlencoded
	 *
	 * @param url
	 *            资源地址
	 * @param params
	 *            参数列表
	 * @param encoding
	 *            编码
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String doPutUrlEncoded(String url, Map<String, String> params, String encoding)
			throws ParseException, IOException {
		String body = "";

		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPut httpPut = new HttpPut(url);

		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		httpPut.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		httpPut.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPut.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPut);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();

		return body;
	}

	/**
	 * 模拟请求，请求方法为delete，请求类型为application/x-www-form-urlencoded
	 *
	 * @param url
	 *            资源地址
	 * @param params
	 *            参数列表
	 * @param encoding
	 *            编码
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String doDeleteUrlEncoded(String url, Map<String, String> params, String encoding)
			throws ParseException, IOException {
		String body = "";

		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);

		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		httpDelete.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		httpDelete.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpDelete.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpDelete);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();

		return body;
	}

	/**
	 * 模拟请求，请求类型为application/json
	 *
	 * @param url 资源地址
	 * @param params 参数列表
	 * @param encoding 编码
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String doPostJson(String url, Map<String, Object> params, String encoding)
			throws ParseException, IOException {
		String body = "";

		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		// 将params转换成json字符串
		JSONObject jsonObject = new JSONObject(params);

		// 设置参数到请求对象中
		httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		String auth = "admin:admin";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpPost.setHeader("Authorization", authHeader);
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("X-XSRF-HEADER", "valid");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();

		return body;
	}

	public static String doGet(String url, Map<String, String> params, String encoding) throws ParseException, IOException {
		String body = "";

		CloseableHttpClient client = HttpClients.createDefault();

		// 拼接url与参数
		url += "?";
		if (params != null && params.size() != 0) {
			for (Entry<String, String> entry : params.entrySet()) {
				url = url + entry.getKey() + "=" + entry.getValue() + "&";
			}
		}
		url = url.substring(0, url.length() - 1);
		HttpGet httpGet = new HttpGet(url);

		String auth = "admin:admin";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpGet.setHeader("Authorization", authHeader);
		httpGet.setHeader("X-XSRF-HEADER", "valid");
		httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpGet);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();

		return body;
	}
}
