# KafkaBench
A project for the course Cloud Service Benchmarking @ TU Berlin, which focuses on benchmarking Apache Kafka that is deployed on AWS.

The benchmark presented in this project focuses on throughput of a Kafka Producer (further interchangeably referred to as producer) and the impact of a subset of producer properties on performance. In particular, the research questions are:

1. What is the effect of an acknowledgement strategy, a batch size and a number of partitions on Apache Kafka Producer’s throughput using commodity hardware?
2. What maximum throughput can be achieved in such a setting?

# Benchmark Design

![benchSpec](https://user-images.githubusercontent.com/22837704/153164377-fe15920b-2963-45bb-ad16-436ee22d192d.jpeg)

On the figure, one can distinguish the following components:

1. Kafka Cluster,
2. Workload Generator,
3. Monitoring Module,
4. Offline-Analysis Module

All components are deployed in AWS. The region of choice is Frankfurt (because of its proximity), which offers three different availability zones.

## Kafka Cluster
Kafka Cluster is set up on three virtual machines. All machines are in the same region but they reside in different availability zones. Each machine hosts a Kafka Broker and a Zookeeper.
Each broker-zookeeper pair is deployed on the general purpose EC2 T2 instance. Specifically, each machine is t2.xlarge.

## Workload Generator

In order to obtain insights about the throughput of Kafka Producer, a configurable workload generator is developed. Similarly to Kafka Cluster,
it is also deployed on t2.xlarge. Each run of a workload generator creates topics and one or more producers with a set of specific properties.
These properties (both for topics and producers) are provided by the use of two .xml files. The desired maximal throughput is defined in one of the files.

Each benchmark runs consists of the following steps:

1. Create topics,
2. Create producers,
3. Run a warm-up phase for 1 minute,
4. Run a benchmark phase for 10 minutes,
5. Save metrics collected during the execution to a file.

During the benchmark run, metrics are collected.

# GitHub Project

This GitHub project consists of the following parts:
1. aws_deployment
2. bench_exe_on_aws
3. ideaProject

## Part 1 - AWS Deployment
This folder consists a series of .sh script that were used to configure the benchmark, i.e. a kafka cluster, a zookeeper quorum, a monitoring stack.
For now, most of the commands were executed manually. Similarly, some machines were provisioned and configured (security groups, etc.) manually. 
For the deployment with the aws cli, please see the file:  kafka_cluster_deployment.sh

## Part 2 - bench_exe_on_aws

The folder contains numerous .yml files grouped into 4 folders. Combinations of kafka settings are workload settings are provided as arguments to the .jar file in order to execute the benchmark. For example:
ava -jar benchmark.jar -k kafkasettings/A0_B16384.yml -w workloadsettings/p10r50k.yml

## Part 3 - ideaProject

This folder contains the .idea project that contains the implementation of workload generator.


