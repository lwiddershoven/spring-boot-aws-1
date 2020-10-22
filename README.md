
Play with the basics of both AWS and the spring boot integration.
In particular, the features that allow me to not have passwords in my code.



## Notes

- Java, tomcat, spring use as default port 8080. The EB proxy `nginx` expects 5000. Even for a java environment.
  Just configure `server.port=5000` in your `application.properties` and all is well. Otherwise you'll get a `502 bad gateway`.
- You can stop and start an environment by using `eb terminate` and `eb restore environment-id`
- `eb restore` needs an environment-**id**. `eb status` and `eb use` want an environment-**name** instead.
  
  The *restore* logs do not print the name, so be sure to grab it from the [console](https://eu-west-1.console.aws.amazon.com/elasticbeanstalk/home?region=eu-west-1#/environments).
It is the name that ends with **-env**.

- The default configuration is single node (no load balancer), but not the free tier. So be sure to [check your costs]() 
  and perhaps manually or automatically [start and stop/terminate and restore](https://aws.amazon.com/premiumsupport/knowledge-center/schedule-elastic-beanstalk-stop-restart/) 
  your environment
- You can have a [warning send](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/monitor_estimated_charges_with_cloudwatch.html) when approaching your budget.   
- Logging issues. If you have [log issues](https://github.com/aws/aws-elastic-beanstalk-cli/issues/37), like the following
   ```
  $ eb logs
  Retrieving logs...
  ERROR: UnicodeDecodeError - 'utf-8' codec can't decode byte 0xe0 in position 1698: invalid continuation byte
  $
   ```
  you can just do
  ```
  $ rm -rf .elasticbeanstalk/logs/
  $ eb logs -a 
  Logs were saved to /home/leon/Development/spring-boot-aws-1/.elasticbeanstalk/logs/201022_135014
  Logs were saved to /home/leon/Development/spring-boot-aws-1/.elasticbeanstalk/logs/201022_135014
  Updated symlink at /home/leon/Development/spring-boot-aws-1/.elasticbeanstalk/logs/latest
  $ ls /home/leon/Development/spring-boot-aws-1/.elasticbeanstalk/logs/latest
  i-0b48809b6f04c2104  i-0ce551befe98d575d
  $ # I apparently collected 2 instances over time. Just check timestamps in both
  $ cat .elasticbeanstalk/logs/latest/i-0 <TAB for autocomplete>
  $ cat .elasticbeanstalk/logs/latest/i-0ce551befe98d575d/var/log/web.stdout.log  # And plenty of other logfiles
    Oct 21 12:05:21 ip-10-15-237-41 web: 2020-10-21 12:05:21.404:INFO::main: Logging initialized @763ms
    Oct 21 12:05:21 ip-10-15-237-41 web: 2020-10-21 12:05:21.474:INFO:oejs.Server:main: jetty-9.2.z-SNAPSHOT
    Oct 21 12:05:21 ip-10-15-237-41 web: 2020-10-21 12:05:21.555:INFO:oejs.ServerConnector:main: Started ServerConnector@5e9c7434{HTTP/1.1}{0.0.0.0:5000}
    ...
  ```
  More work, but at least you get info. And yes, your `nginx` logs are here too!
- The default instance type, `t1.micro` is part of the [free tier](https://aws.amazon.com/free/?all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&all-free-tier.q=EC2&all-free-tier.q_operator=AND)
  but I have had my account for longer, so I changed the instance type to (see Configuration, Capacity management) `t2.nano`. But 
  that fails because I apparently need a VPC. And testing `t2.micro` yields also a failure. t1 it is.