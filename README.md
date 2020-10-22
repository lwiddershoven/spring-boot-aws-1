
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
