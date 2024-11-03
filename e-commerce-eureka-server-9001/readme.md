
# 为什么需要集群 Eureka Server
说明:
1. 微服务 RPC 远程服务调用最核心的是实习高可用
2. 如果注册中心只有1个，它出故障，会导致整个服务环境不可用
3. 解决办法: 搭建 Eureka 注册中心集群，实习 **负载均衡 + 故障容错** 


# 想要进行域名解析的话，我们需要配置 本地主机的 hosts 文件

C:\Windows\System32\drivers\etc
文件: C:\Windows\System32\drivers\etc\host
2.  文件可以先拷贝到桌面，修改后，再拷贝会去
3.  加入内容:
#eureka  主机名和 ip 映射
127.0.0.1 eureka9001.com
127.0.0.1 eureka9002.com

```
text

# Copyright (c) 1993-2009 Microsoft Corp.
#
# This is a sample HOSTS file used by Microsoft TCP/IP for Windows.
#
# This file contains the mappings of IP addresses to host names. Each
# entry should be kept on an individual line. The IP address should
# be placed in the first column followed by the corresponding host name.
# The IP address and the host name should be separated by at least one
# space.
#
# Additionally, comments (such as these) may be inserted on individual
# lines or following the machine name denoted by a '#' symbol.
#
# For example:
#
#      102.54.94.97     rhino.acme.com          # source server
#       38.25.63.10     x.acme.com              # x client host

# localhost name resolution is handled within DNS itself.
#	127.0.0.1       localhost
#	::1             localhost
 
127.0.0.1 account.wondershare.com
# 配置 eureka 主机 和 ip 的映射
127.0.0.1 eureka9001.com
127.0.0.1 eureka9002.com

```

