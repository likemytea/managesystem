1. pom

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- mybatis的分页插件 -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.1.4</version>
        </dependency>
        
2.
 #mybatis
mybatis.mapper-locations=classpath:mapping/*.xml
mybatis.type-aliases-package=com.chenxing.managesystem.domain
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
3.启动类追加
@MapperScan("com.chenxing.managesystem.mapper")
4.
UserMapper.xml          
UserMapper.java
User.java
service等等
5.结束