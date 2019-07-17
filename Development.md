# Development 20190716技术中心

[TOC]

记录开发过程中一些实现层面的方法和变更。

## V2.1(版本号待定)

### 多部署模式

数据湖平台支持两种部署模式：

1. **一个平台一套用户**
适用于一个组织机构（机关、企事业单位、社会团队等）部署一个数据湖平台，并安装一个或多个大数据集群。

2. **一个平台多套用户**
云的模式，部署一个数据湖平台，服务于多个组织机构，为每个组织机构安装一个（也可能多个）大数据集群。

安全模块为自动化部署提供API，主要包括：

1. **创建Realm**
在Foundation中创建Realm，Realm表示一个组织机构，可以管理一个组织机构的用户。

2. **创建用户**
在Foundation中创建管理员用户。

3. **添加用户到集群**
在安装大数据集群阶段，将Foundation中的用户同步到集群中，包括Ambari中设置集群权限，在HDFS中创建用户目录，添加到ElasticSearch。

#### 一个平台一套用户

描述一个平台一套用户的模式下，自动化部署所调用的API示例，包括Foundation用户初始化和大数据集群用户初始化。

（只有可执行的API实例，对应的API详细说明见RESTAPI.md）

**Foundation用户初始化**

在Foundation部署时初始化用户，包括创建Realm及管理员用户，给一个组织机构使用。

1.创建Realm

```
curl "http://10.111.24.82:9000/indata-manage-portal/service/api/manage/tenants/realm" \
-X POST -d '{"realm":"realmx","singleUserMultiClustersMode":"1","token":"eyJhbGciOiJSUzI1NiIsInR5cC..."}' \
-H "Content-type: application/json"
```

2.创建管理员用户

```
curl "http://10.111.24.82:9000/indata-manage-portal/service/api/manage/tenants/user" \
-X POST -d '{"userId":"tenantx","userName":"tenantx","passWord":"tenantx","tenantRealm":"realmx","isClusterAdmin":"1","token":"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2..."}' \
-H "Content-type: application/json"
```

**大数据集群用户初始化**

每安装一个大数据集群，添加指定用户（已有Foundation用户）到大数据集群

```
curl "http://10.111.24.82:9000/indata-manage-portal/service/api/manage/tenants/clusteruser" \
-X POST -d '{"userId":"tenantx","tenantRealm":"realmx","clusterId":"cluster8865","isClusterAdmin":"1","token":"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ2QkpjaF9YbU9vaVBf..."}' \
-H "Content-type: application/json"
```

#### 一个平台多套用户

描述一个平台多套用户的模式下，自动化部署所调用的API示例，包括Foundation用户初始化和大数据集群用户初始化。

**Foundation用户初始化**

在Foundation部署时初始化用户，包括创建Realm及管理员用户，给一个组织机构使用。

1.创建Realm
同一个平台一套用户

2.创建管理员用户
同一个平台一套用户


**大数据集群用户初始化**

安装大数据集群时，先判断是否为新的组织机构安装，如果是新的组织机构，先进行Foundation用户初始化，再添加指定用户（已有Foundation用户）到大数据集群。否则，直接添加用户到大数据集群。


## V2.0
无重大变更。

## V1.5

### 内置用户

【V1.5开始】``thriftaction_.*`` 以thriftaction_前缀，用于SparkSQL资源授权

【V1.3开始】 ``knoxadmin``（tenant realm）

【V1.0开始】 ``superadmin`` ``codeadmin``（master realm），``nifiadmin``（tenant realm）

### 内置组 标识租户

``TenantGroup-*`` 以TenantGroup-为前缀，表示该组是一个租户。

### 内外网环境

#### 概述
所有在浏览器重定向的地址，都是外网地址，且与用户第一次访问平台所输入的URL的地址相同。由于外网地址可能是平台部署完成后绑定的，且有可能会更改，所以应用的外网地址存储和获取不宜使用配置文件的方式。

因为根据用户第一次访问平台所输入的URL获取外网地址，所以要求平台的各个模块都反向代理为同一个访问地址。

sdk.security.util.PathBasedKeycloakConfigResolver用于解析认证相关配置，以将未登录的应用重定向到正确的登录页面。安全模块认证部分支持内外网环境的相关实现也在其中。

另外，应用中配置文件中的地址都为内网地址，用于应用之间的服务调用。

#### 使用说明

默认开启支持内外网部署。

Keycloak的端口，默认使用Keycloak.json中的配置，即19080。当使用外网其他端口时，在conf.properties中添加

```
network.keycloak.port=19080
```

#### 开发环境说明

本地开发环境中，如果不在本地部署认证中心，就需要关闭内外网环境的支持，在conf.properties中添加

```
network.in-external=false
```

这样配置后，认证中心的地址将读取应用中所配置的keycloak.json。

自动部署时，这个配置项被统一修改为true。
 
#### 帮助与文档 开发配置hosts文件
开发者门户帮助与文档中，开发配置中的hosts，用于开发Hadoop client时，配置在运行Hadoop client的机器上。

hosts文件的内容为大数据集群各个节点IP和域名的映射。之前的版本，在OpenStack内外网环境中，IP地址为外网。由于外网地址的不确定性，在V1.5中，IP地址为内网地址，由使用者自行修改。

### LDAP
Keycloak用户同步到LDAP时，增加了email（Keycloak user的email）和givenName（Keycloak user的firstName）两个LDAP Mappers。

email映射，LDAP entry需要增加objectClass: `extensibleObject`，即Keycloak User Federation的User Object Classes增加extensibleObject，即`krbPrincipal,krbPrincipalAux,krbTicketPolicyAux,posixAccount,extensibleObject`。

【重要】升级时，需要先修改已有LDAP数据，给每个entry增加objectClass，否则已有用户的firstName属性会丢失，可以使用shell批量处理：`/indata-keycloak-sql/1.5u/ldap_add_extensibleobject.sh`。修改后，再执行Keycloak update sql。

### SQL
新增SQL语句时，必须利于自动化升级，详见/indata-keycloak-sql/命名规范。

### 平台的Cookies

#### V1.5平台的Cookies

##### 新增的Cookie
管理员门户：`manage-sg-user`

开发者门户：`dev-sg-user`

用于Elasticsearch Kibana的Proxy Authentication（Search Guard），并借助Nginx附加到每个Kibana请求的Header中（每个ES集群对应两个nginx server）。

##### 修改的Cookie

`mt`改为`tenantrealm`，cookie name在keycloak.json中配置。这样，不同Keycloak Realm保护的应用可以同时在一个浏览器访问。

#### V1.0/V1.3平台的Cookies

管理员门户:

`clustername` 集群名称，Ambari UI取此值，展示指定集群的信息

`hadoop-jwt` Token，用于Ambari UI单点登录

开发者门户：

`{realm}-jwt` Token，用于NiFi UI单点登录

`mt` tenant realm，用于多租户的用户登录

### Web安全增强

#### SecurityHttpServletRequestWrapper

实现在security-keycloak-sdk中，通过Request Wrapper，对一些特殊字符（单引号、圆括号、尖括号等）进行编码。

默认开启，如果关闭的话，在SDKFilter中添加init-param参数，name为securityRequestWrapper，value为false。

```
<filter>
	<filter-name>SDKFilter</filter-name>
	<filter-class>
		sdk.security.filter.SDKFilter
	</filter-class>
	<init-param>
		<param-name>securityRequestWrapper</param-name>
		<param-value>false</param-value>
	</init-param>
</filter>
```

基于特殊字符的处理，会对原本就需要这些字符的请求产生影响。可以将这些请求URL排除（.*表示任意字符，多个用分号隔开）：

```
<init-param>
	<param-name>securityRequestWrapperExcludes</param-name>
	<param-value>.*/service/manage/security/user/now.*</param-value>
</init-param>
```

#### SecurityRefererValidator

实现在security-keycloak-sdk中，校验Request referer。

默认开启，如果关闭的话，在SDKFilter中添加init-param参数，name为securityRequestWrapper，value为false。

```
<filter>
	<filter-name>SDKFilter</filter-name>
	<filter-class>
		sdk.security.filter.SDKFilter
	</filter-class>
	<init-param>
		<param-name>securityRefererValidator</param-name>
		<param-value>false</param-value>
	</init-param>
</filter>
```

可以在war中conf.properties内配置允许的referer，如允许从所有来源访问：
```
security.referer=.*
```

#### Nginx

在tomcat-master和tomcat-tenant的nginx server中，添加：

```
#安全配置
##禁止服务器自动解析资源类型
add_header X-Content-Type-Options 'nosniff';
##防XSS攻击
add_header X-Xss-Protection '1; mode=block';
add_header Content-Security-Policy "default-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:; frame-src *;";
##允许的HTTP method
if ($request_method !~ ^(GET|POST|PUT|DELETE)$) {
	return 405;
}
```

#### Cookie

* 添加HttpOnly属性，需要在前端获取的除外（如管理员门户中clustername，用于服务管理菜单）。

* 尽量避免使用Persistent Cookie，而是使用会话Cookie。

#### Keycloak

启用redirect_uri校验。

在MySQL的keycloak数据中执行，执行前先根据实际环境修改IP地址，执行后重启Keycloak或在Keycloak控制台上清除Cache。

```
-- master realm
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127:9000/*' WHERE (`CLIENT_ID`='32d28b21-707c-4b9c-91eb-33914444b071') AND (`VALUE`='*') LIMIT 1;
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127:9000/*' WHERE (`CLIENT_ID`='a57d10e9-05ad-4505-82db-93efe0b4e58c') AND (`VALUE`='*') LIMIT 1;
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127:9000/*' WHERE (`CLIENT_ID`='59a37d97-76f9-4544-ac8d-8b741ef41f7e') AND (`VALUE`='*') LIMIT 1;
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127:9000/*' WHERE (`CLIENT_ID`='2b0e8923-29cd-4fe6-809c-58a653e94422') AND (`VALUE`='*') LIMIT 1;
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127:9000/*' WHERE (`CLIENT_ID`='684f1c98-2fc2-45ec-bb27-be7668e7b11b') AND (`VALUE`='*') LIMIT 1;

-- 其他realm
UPDATE `redirect_uris` SET `VALUE`='http://10.110.13.127/*' WHERE (`VALUE`='*');
```

### 依赖

security-keycloak-sdk使用的版本是1.1.0-SNAPSHOT，封版时没有替换成release（其他模块也一样）。

maven私服中上传了release，为1.1.0。

并且，两者完全相同，即：
/com/inspur/security/security-keycloak-sdk/1.1.0-SNAPSHOT/security-keycloak-sdk-1.1.0-20180922.145544-10.jar
/com/inspur/security/security-keycloak-sdk/1.1.0/security-keycloak-sdk-1.1.0.jar

### URL简化

对平台访问URL进行简化。

```
管理员门户
http://10.110.13.26:9000/indata-manage-portal/?realm=master
简化为：
http://10.110.13.26:9000/manage

开发者门户
http://10.110.13.26/indata-dev-portal/?realm=realm7837
简化为：
http://10.110.13.26/dev/?realm=realm7837
```

#### 实现方法

##### 更改war包名字
indata-manage-portal改为manage、indata-dev-portal改为dev

##### Nginx
在`location / {} `前添加（以manage为例，dev亦然）

```
location /indata-manage-portal {
	proxy_pass   http://tomcat_master/manage;
	proxy_http_version 1.1; # this is essential for chunked responses to work
	proxy_buffering    off;
	client_max_body_size 1024m;
	proxy_set_header   X-Real-IP $remote_addr;
	proxy_set_header   X-Scheme $scheme;
	proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
	proxy_set_header   Host $http_host;
	proxy_set_header   X-Forwarded-Proto  $scheme;
}
```

这样，其他模块调用portal的REST API，依然可以使用/indata-manage-portal这个上下文。

##### Keycloak
最小修改原则，indata-manage-portal和indata-dev-portal这两个应用对应的Client ID不做修改。

两者对应的client，Admin URL属性中的上下文分别改为manage和dev。

## V1.2

**发布的正式版本号为V1.3**

### JAVA SSH
程序中，有时需要远程连接sshd服务器，执行一些命令或者传输文件。

连接时需要验证用户身份。

在V1.0以及之前的版本中，manage-common-util.jar（[manage-common模块](http://10.110.13.73:8080/tfs/DefaultCollection/Inspur%20Cloud%20Platform/_git/manage-common)）提供的接口，使用的是root用户及密码进行ssh认证。其他模块中可能有调用该接口，可能有自己实现的ssh连接，也使用了root用户及密码。之后的版本将不再提供root密码，所以需要修改ssh连接方式。

在V1.2中，统一使用indatamgr用户及公钥进行ssh连接的方式。接口描述可参照[SshUtil接口描述](http://10.110.13.73:8080/tfs/DefaultCollection/Inspur%20Cloud%20Platform/_git/manage-common?path=%2FREADME.md&version=GBmaster&_a=contents)。

### Keytab文件的获取

#### 平台用户Keytab文件
*（与v1.0没有变化）*

com.inspur.bigdata.manage.cluster.kerberos.api.KerberosClusterService

##### 引用

```
<dependency>
	  <groupId>com.inspur.bigdata.manage.cluster</groupId>
	  <artifactId>manage-cluster-hadoop-api</artifactId>
	  <version>1.2.0-SNAPSHOT</version>
</dependency>
```

##### 获取文件路径

```
/**
 * 获取Kerberos相关信息 1.将文件下载到本地； 2.返回本地文件的路径.
 * 
 * @param userId 用户ID(带有后缀)
 * 
 * @return Map，key分别为：keytab[keytab的文件路径],krb5[krb5.conf的文件路径],principal[principal名称]
 * @throws Exception
 */
public static Map<String, Object> getKerberosInfo(String userId) throws Exception
```

##### 获取文件流

```
/**
 * 获取Kerberos相关信息 1.将文件下载到本地； 2.返回本地文件的路径.
 * 
 * @param userId 用户ID(带有后缀)
 * 
 * @return Map，key分别为：keytab[keytab文件流],krb5[krb5.conf文件流],principal[principal名称]
 * @throws Exception
 */
public static Map<String, Object> getKerberosInfoBytes(String userId) throws Exception
```

#### 服务的Keytab文件
调用SshUtil接口获取（待定）

### Keytab文件的生成
#### 用户的Keytab文件
创建用户时，执行Kerberos change_password为用户设置Kerberos密码，但不生成Keytab文件，仅在需要使用时再生成。Foundation的任一节点，都能生成用户的Keytab文件。

这样，Foundation无需保存平台用户的keytab文件，更有利于KDC的HA部署。

#### 服务的Keytab文件
服务的Keytab文件，在创建集群安装大数据组件时生成，存放在集群的相应节点上。

### 用户
`superadmin`用户作为平台管理员（Foundation管理员），不再作为具体某一个集群的管理员。superadmin用户在Foundation安装时创建，可以登录Keycloak控制台，做一些安全管理相关的配置。

`codeadmin`与`superadmin`用户类似，但它用在程序内部。程序调用Keycloak的API，做一些如创建realm、管理用户等操作时，使用codeadmin用户的Access Token。

集群管理员，在创建大数据集群时进行创建。

## V1.0
