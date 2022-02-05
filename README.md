# KafkaBench
A project for the course Cloud Service Benchmarking @ TU Berlin, which focuses on benchmarking Apache Kafka that is deployed on AWS.

This GitHub project consists of the following parts:
1. aws_deployment
2. bench_exe_on_aws
3. ideaProject
4. kafka_docker_for_local_test

## Part 1 - AWS Deployment
This folder consists a series of .sh script that were used to configure the benchmark, i.e. a kafka cluster, a zookeeper quorum, a monitoring stack.
For now, all commands were executed manually. Similarly, machines were provisioned and configured (security groups, etc.) manually. 

## Part 2 - bench_exe_on_aws

The folder contains numerous .yml files grouped into 4 folders. Combinations of kafka settings are workload settings are provided as arguments to the .jar file in order to execute the benchmark. For example:
ava -jar benchmark.jar -k kafkasettings/A0_B16384.yml -w workloadsettings/p10r50k.yml
