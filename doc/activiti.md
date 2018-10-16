https://blog.csdn.net/wanbf123/article/details/78222988
1. 引入activiti依赖包， 只有一个。
2. 注入为我们自动配置好的服务 ，见 ActivitiService.java
3. bpmn文件制作和放置
绘制流程图建议使用activiti-explorer，路径是activiti-5.22.0\wars\activiti-explorer.war，将war放入tomcat，浏览器打开，在“流程”里绘图然后导出。idea的activiti插件绘图，支持不好。
在resources下新建processes文件夹，将bpmn、bpmn.xml或bpmn20.xml（activiti-explorer流程图导出文件）放入其中
4. 系统启动时会初始化activiti自带的表结构。


================
xml 文件说明解析：
1.