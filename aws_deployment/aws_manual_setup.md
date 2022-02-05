# AWS Setup via AWS console
## PART 1: Setting up a zookeeper server

1. Set region for Frankfurt, \
2. Go to VPC -> Subnets -> Note down CIDRs of Subnets, \
3. Launch instance: \
    3.1 Choose AMI: Ubuntu 18.04 \
    3.2 Choose instance type: t2.medium \
    3.3 Configure instance details:  \
        3.3.1 Configure subnet: For the first instance, choose the first availability zone from Step 2 i.e. in my AWS console the firs range of IP addresses - 172.31.0.0/20 maps to the A.Z eu-central-1c \
        3.3.2 Configure network interface:  \
    3.4 Add storage: For now leave 8gb in order to avoid additional costs during the setup. This will be increased for the benchmark run \
    3.5 Add Tags: Add Tage called Name: server1 \
    3.6 Add a security group: \
    Type        | Port  | Source        | Comment \
    SSH         | 22    | MyIP          | SSH machines \
    Custom TCP  | 2181  | MyIP          | SSH zookeeper \
    Custom TCP  | 2181  | 172.31.0.0/16 | zookeeper \
    Custom TCP  | 2888  | 172.31.0.0/16 | zookeeper \
    Custom TCP  | 3888  | 172.31.0.0/16 | zookeeper \
    Custom TCP  | 9092  | 172.31.0.0/16 | kafka \ 
    Custom TCP  | 7075  | 172.31.0.0/16 | jmx \

## PART 2: Setting up a zookeeper quorum (3 machines)

1. Create an image of the existing instance. Go to instances menu, right click, create an image, \
2. Go to the Images window. Choose AMIs. Create server2 and server3 from theme. Important: assign different availabily zones to each new subnet, so that 172.31.22.2 and 172.31.33.3 are in respective ranges. \

## PART 3: Setting up a kafka cluster 
1. Go to volume and create three 10gb volumes, \
2. Attach a volume with a given availability zone to the corresponding server, \
3. Update the security group so that it contains 9092 from 172.31.0.0/16 and MyIP. \

## PART 4: Setting up a monitoring stack
1. Create a new instance, t2.micro with 8GB ram, \
2. Create a new security group: \
    Type        | Port  | Source        | Comment \
    SSH         | 22    | MyIP          | SSH to machines \
    Custom TCP  | 9090  | 172.31.0.0/16 | Prometheus \

3. 

