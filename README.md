git
=====
git config --global user.name  'laotitouzi'  
git config --global user.email  442748419@qq.com  

git remote add org git@github.com:laotitouzi/web.git  
git add src pom.xml  增加到本地仓库  
  
git commit -m 'first commit'   //提交到本地仓库  

git fetch org  
git merge org/master  

git diff  

git push org master -f   //提交到github远程仓库	  

maven命令
======
mvn clean package -Dmaven.test.skip=true  
mvn dependency:sources   
mvn dependency:tree  
mvn eclipse:eclipse  
mvn jetty:run   

activeMQ
===== 
127.0.0.1:8161/admin/ admin/admin  mq管理平台  
activemq.bat ACTIVEMQ_OPTS配置   
-Dorg.apache.activemq.SERIALIZABLE_PACKAGES="java.lang,java.util,org.apache.activemq,org.fusesource.hawtbuf,com.thoughtworks.xstream.mapper,com.mycompany.myapp"  
-Dorg.apache.activemq.SERIALIZABLE_PACKAGES="*"  允许序列化  