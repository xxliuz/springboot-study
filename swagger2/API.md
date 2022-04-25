# SpringBoot2 中使用Swagger2 构建RESTful APIs


<a name="overview"></a>
## 概览
This a demo for Swagger2


### 版本信息
*版本* : 1.0


### 联系方式
*名字* : zhou.liu


### 许可信息
*服务条款* : http://127.0.0.1:9090/v2/api-docs


### URI scheme
*域名* : localhost:9090  
*基础路径* : /


### 标签

* test-controller : Test Controller




<a name="paths"></a>
## 资源

<a name="test-controller_resource"></a>
### Test-controller
Test Controller


<a name="getusernameusingget_7"></a>
#### /getUserName 根据用户id获得用户的姓名
```
GET //getUserName
```


##### 说明
id不能为空


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**userId**  <br>*必填*|用户id|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[JsonResult](#jsonresult)|
|**204**|No Content|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
//getUserName
```


###### 请求 query
```
json :
{
  "userId" : "string"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : "string",
  "data" : "object",
  "message" : "string"
}
```


<a name="hellousingget_7"></a>
#### /hello 欢迎入口
```
GET //hello
```


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|string|
|**204**|No Content|无内容|
|**401**|Unauthorized|无内容|
|**403**|Forbidden|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
//hello
```


##### HTTP响应示例

###### 响应 200
```
json :
"string"
```




<a name="definitions"></a>
## 定义

<a name="jsonresult"></a>
### JsonResult

|名称|说明|类型|
|---|---|---|
|**code**  <br>*可选*|**样例** : `"string"`|string|
|**data**  <br>*可选*|**样例** : `"object"`|object|
|**message**  <br>*可选*|**样例** : `"string"`|string|





