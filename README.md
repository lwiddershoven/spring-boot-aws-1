
Play with the basics of both AWS and the spring boot integration.
In particular, the features that allow me to not have passwords in my code.

## Secrets Manager & Parameter Store

The [current pricing](https://aws.amazon.com/secrets-manager/pricing/) is $0.40 per secret, per month.
So, cheap enough to play with if you remove all the stuff you don't need anymore. Because all that small stuff adds up.

The [AWS Systems Manager Parameter Store](https://aws.amazon.com/systems-manager/pricing/) (search for Parameter Store) pricing
is 
- 	$0.05 per 10,000 Parameter Store API interactions
- 	$0.05 per 10,000 Parameter Store API interactions 
which has the very nice feature that it is free if your test application is not running. 

The definition of a parameter can be found [here](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html#what-is-a-parameter)
 
It seems that the Parameter Store can manage SecretString. In that case you get a charge from the Key Management Store, which 
is $0.03 per 10,000 requests. For 'generate' requests it is more expensive but these should be rare.
Again, if you don't request keys it is free, so ideal for testing features.

I personally really like the Secret Manager for secrets as the name makes the scope clear, it has secret rotation, it 
does not really delete secrets until sometime afterward (really useful if something went wrong with your db credentials)
and you do not have to give IAM permissions to the consumer on the encryption keys, just on the secret.
It would however be nice if the first 5 secrets were free though :) 
Let's see if this preliminary opinion still holds after I played with both.


## Spring Cloud AWS

There is a Spring module for AWS integration that integrates nicely with the configuration ; it should be able to
retrieve database credentials and the like from the various AWS services before the application is properly started.

Unfortunately, this module [requires xml configuration](https://docs.spring.io/spring-cloud-aws/docs/current/reference/html/#amazon-sdk-configuration).
That is very much survivable (I have worked with that in the old days), it is pretty easy to integrate with the 
Spring lifecycle so I do not currently see the added value of the Spring cloud aws libraries for this project.
At this time I prefer the  boilerplate AWS java libs (with AWS documentation, Stackoverflow and reddit support).

For that to work, I'm going to base myself on [these Amazon AWS java examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/java/example_code)
and [appropriate javadocs](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/simplesystemsmanagement/model/GetParameterRequest.html).
Yes people. Real developers do read documentation. On occasion at least. You heard it here first.

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
  