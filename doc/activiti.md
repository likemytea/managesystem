https://blog.csdn.net/wanbf123/article/details/78222988
https://www.cnblogs.com/ymf23/p/7001307.html
1. 引入activiti依赖包， 只有一个。
2. 注入为我们自动配置好的服务 ，见 ActivitiService.java
3. bpmn文件制作和放置
绘制流程图建议使用activiti-explorer，路径是activiti-5.22.0\wars\activiti-explorer.war，将war放入tomcat，浏览器打开，在“流程”里绘图然后导出。idea的activiti插件绘图，支持不好。
在resources下新建processes文件夹，将bpmn、bpmn.xml或bpmn20.xml（activiti-explorer流程图导出文件）放入其中
4. 系统启动时会初始化activiti自带的表结构。


================
1. 流程引擎表结构说明
ACT_RE_*: 'RE'表示repository。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。
ACT_RU_*: 'RU'表示runtime。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。 Activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。
ACT_ID_*: 'ID'表示identity。 这些表包含身份信息，比如用户，组等等。
ACT_HI_*: 'HI'表示history。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。
ACT_GE_*: 通用数据， 用于不同场景下，如存放资源文件。
太细了，就不说了，自己用的时候亲自体会会了解的更深刻。