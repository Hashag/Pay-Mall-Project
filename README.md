# Pay-Mall-Project
the default branch is pay module.

the link to download the htmls: https://imcfile.oss-cn-beijing.aliyuncs.com/shizhan/file/392/dist.zip

other configures may need:

Run RabbitMQ in Docker:
docker run -d -p 5672:5672 -p 15672:15672 registry.cnshanghai.aliyuncs.com/springcloud-imooc/rabbitmq:3.8.2-management
https://imcfile.oss-cn-beijing.aliyuncs.com/shizhan/file/liaoshixiong/nginx1.16.1.tar.gz

Run Redis in Docker:
docker run -d -p 6379:6379 redis:5.0.7

Get JDK:
wget https://imcfile.oss-cn-beijing.aliyuncs.com/shizhan/file/liaoshixiong/jdk-8u231-linuxx64.tar.gz

Centos Service Template:
[Unit]
Description=mall
After=syslog.target
[Service]
User=root
ExecStart=/usr/local/jdk1.8.0_231/bin/java -jar -Dspring.profiles.active=prod
/root/mall.jar
[Install]
WantedBy=multi-user.target
