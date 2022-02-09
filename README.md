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

## Part 3 - ideaProject

This folder contains the .idea project that contains the implementation of workload generator


Some extra info

I first set up the project manually using AWS console in order to speed up the process. 
Thus, the file aws manual setup contains the description of the performed steps. I later managed to get a part of it by using aws-cli.
The result of these endeavour can be seen in the file kafka_cluster_deployment.sh.

Here's the list of what I did. I set all machines and security groups according to the file aws_manual_set or partly kafka_cluster_deployment.
For the kafka cluster:
Then, on each machine I executed:
1. general/setup.sh
2. zookeper/zookeper_setup.sh (also adjusted zookeper.properties to my needs)
3. kafka/kafka_setup.sh (also adjusted server.properties to my needs)
4. kafka_disk_setup.sh
5. tested the cluster with kafka_zoo_setuptest/setup_test.sh

For the monitoring machine:
1. First prometheus setup monitoring/prometheus/prometheus-setup.sh
2. Then grafana setup monitoring/grafana/graphana-setup.sh

For the workload: please see workload_generation.sh
