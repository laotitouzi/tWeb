git
=====
git config --global user.name  'laotitouzi'  
git config --global user.email  442748419@qq.com  
ssh-keygen -t rsa -C "442748419@qq.com"   
按3个回车，密码为空。(不要输密码)     
然后到.ssh下面将id_rsa.pub里的内容复制出来粘贴到github个人中心的账户设置的ssh key里面       

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
