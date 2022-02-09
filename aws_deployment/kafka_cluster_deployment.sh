# assumption: aws configure has been executed successfully and a key pair has already been created, for example using
# aws console

UBUNTU_IMAGE="ami-042ad9eec03638628"
KEY_NAME="keyname"
KAFKA_MACHINE="t2.xlarge"
WORKLOAD_MACHINE="t2.xlarge"
MONITORING_MACHINE="t2.micro"
REGION="eu-central-1"

MY_IP=`curl http://whatismyip.akamai.com/`

# First availability zone
AZ1="eu-central-1a"
CIDR_AZ1="172.31.16.0/20"
SUBNET_AZ1=$(aws ec2 describe-subnets --filter Name=availability-zone,Values=eu-central-1a --output text --query "Subnets[].SubnetId")
# second
AZ2="eu-central-1b"
CIDR_AZ2="172.31.32.0/20"
SUBNET_AZ2=$(aws ec2 describe-subnets --filter Name=availability-zone,Values=eu-central-1b --output text --query "Subnets[].SubnetId")

AZ3="eu-central-1c"
CIDR_AZ3="172.31.0.0/20"
SUBNET_AZ3=$(aws ec2 describe-subnets --filter Name=availability-zone,Values=eu-central-1c --output text --query "Subnets[].SubnetId")

# get VPC id
VPC_ID=$(aws ec2 describe-vpcs --output text --query "Vpcs[].VpcId")

#######################################
##### PART 1 - Setting up kafka cluster
#######################################

# create a new security group and get its id
SEC_GROUP_ID=$(aws ec2 create-security-group --group-name kafka-zoo-cli --vpc-id $VPC_ID --description "Security group for zoo and kafka, made with cli" --output text --query "GroupId")
SG_ACCESSOR="sg-"
SEC_GROUP_NAME=$(aws ec2 describe-security-groups --filter Name=group-id,Values=$SEC_GROUP_ID --output text --query "SecurityGroups[].GroupName")

# configure security groups
# ssh from local machine
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 22 --cidr $MY_IP/32
# kafka
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 9092 --cidr 172.31.0.0/16
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 9092 --cidr $MY_IP/32
# zoo
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 3888 --cidr 172.31.0.0/16 
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 2888 --cidr 172.31.0.0/16
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 2181 --cidr 172.31.0.0/16
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 2181 --cidr $MY_IP/32

# prometheus
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 7075 --cidr 172.31.0.0/16

# run first zoo-kafka server in first AZ within the corresponding subnet
SERVER1=$(aws ec2 run-instances \
    --image-id $UBUNTU_IMAGE \
    --instance-type $KAFKA_MACHINE \
    --count 1 \
    --placement AvailabilityZone=$AZ1 \
    --key-name $KEY_NAME \
    --security-group-ids $SEC_GROUP_NAME \
    --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=server1}]' \
    --output text --query "Instances[].InstanceId")

SERVER2=$(aws ec2 run-instances \
    --image-id $UBUNTU_IMAGE \
    --instance-type $KAFKA_MACHINE \
    --count 1 \
    --placement AvailabilityZone=$AZ2 \
    --key-name $KEY_NAME \
    --security-group-ids $SEC_GROUP_NAME \
    --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=server1}]' \
    --output text --query "Instances[].InstanceId")


SERVER3=$(aws ec2 run-instances \
    --image-id $UBUNTU_IMAGE \
    --instance-type $KAFKA_MACHINE \
    --count 1 \
    --placement AvailabilityZone=$AZ3 \
    --key-name $KEY_NAME \
    --security-group-ids $SEC_GROUP_NAME \
    --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=server1}]' \
    --output text --query "Instances[].InstanceId")

PUBLIC_DNS_SER1=$(aws ec2 describe-instances --instance-ids $SERVER1 --output text --query "Reservations[].Instances[].PublicDnsName")
PUBLIC_DNS_SER2=$(aws ec2 describe-instances --instance-ids $SERVER2 --output text --query "Reservations[].Instances[].PublicDnsName")
PUBLIC_DNS_SER3=$(aws ec2 describe-instances --instance-ids $SERVER3 --output text --query "Reservations[].Instances[].PublicDnsName")

ssh-keyscan -H $PUBLIC_DNS_SER1 >> ~/.ssh/known_hosts
ssh-keyscan -H $PUBLIC_DNS_SER2 >> ~/.ssh/known_hosts
ssh-keyscan -H $PUBLIC_DNS_SER3 >> ~/.ssh/known_hosts

PUBLIC_IP_S1=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER1" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][1]' --output text)

PUBLIC_IP_S2=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER2" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][1]' --output text)

PUBLIC_IP_S3=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER3" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][1]' --output text)

PRIV_IP_S1=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER1" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][0]' --output text)

PRIV_IP_S2=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER2" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][0]' --output text)

PRIV_IP_S3=$(aws --region $REGION ec2 describe-instances \
    --filters "Name=instance-state-name, Values=running" "Name=instance-id, Values=$SERVER3" \
    --query 'Reservations[*].Instances[*].[PrivateIpAddress, PublicIpAddress][0][0]' --output text)

# copy basic setup to each machine
scp -i $KEY_NAME general/setup.sh ubuntu@$PUBLIC_DNS_SER1:/home/ubuntu
scp -i $KEY_NAME general/setup.sh ubuntu@$PUBLIC_DNS_SER2:/home/ubuntu
scp -i $KEY_NAME general/setup.sh ubuntu@$PUBLIC_DNS_SER3:/home/ubuntu

ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S1 'chmod +x setup.sh && ./setup.sh'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S2 'chmod +x setup.sh && ./setup.sh'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S3 'chmod +x setup.sh && ./setup.sh'


# 4. Mock DNS
# We have three subnets in the VPC
# In each subnet there is one kafka - zookeeper pair
# First subnet is given by 172.31.0.0/20
# Second by 172.31.16.0/20
# Third by 172.31.32.0/20
# Thus, we have to choose an IP for each pair in the corresponding range
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S1 'echo "
$PRIV_IP_S1 kafka1
$PRIV_IP_S1 zookeeper1
$PRIV_IP_S2 kafka2
$PRIV_IP_S2 zookeeper2
$PRIV_IP_S3 kafka3
$PRIV_IP_S3 zookeeper3
" | sudo tee -a /etc/hosts'

ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S2 'echo "
$PRIV_IP_S1 kafka1
$PRIV_IP_S1 zookeeper1
$PRIV_IP_S2 kafka2
$PRIV_IP_S2 zookeeper2
$PRIV_IP_S3 kafka3
$PRIV_IP_S3 zookeeper3
" | sudo tee -a /etc/hosts'

ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S3 'echo "
$PRIV_IP_S1 kafka1
$PRIV_IP_S1 zookeeper1
$PRIV_IP_S2 kafka2
$PRIV_IP_S2 zookeeper2
$PRIV_IP_S3 kafka3
$PRIV_IP_S3 zookeeper3
" | sudo tee -a /etc/hosts'

scp -i $KEY_NAME -r zookeeper ubuntu@$PUBLIC_DNS_SER1:/home/ubuntu/zookeeper_conf
scp -i $KEY_NAME -r zookeeper ubuntu@$PUBLIC_DNS_SER2:/home/ubuntu/zookeeper_conf
scp -i $KEY_NAME -r zookeeper ubuntu@$PUBLIC_DNS_SER3:/home/ubuntu/zookeeper_cong

ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S1 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S2 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S3 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'



scp -i $KEY_NAME -r kafka ubuntu@$PUBLIC_DNS_SER1:/home/ubuntu/kafka_conf
scp -i $KEY_NAME -r kafka ubuntu@$PUBLIC_DNS_SER2:/home/ubuntu/kafka_conf
scp -i $KEY_NAME -r kafka ubuntu@$PUBLIC_DNS_SER3:/home/ubuntu/kafka_cong

# create volumes for kafka logs
VOLUME_AZ1=$(aws ec2 create-volume --availability-zone $AZ1 --size 25 --output text --query "VolumeId")
VOLUME_AZ2=$(aws ec2 create-volume --availability-zone $AZ2 --size 25 --output text --query "VolumeId")
VOLUME_AZ3=$(aws ec2 create-volume --availability-zone $AZ3 --size 25 --output text --query "VolumeId")

### In order to further set up the cluster, you have to:
# Change zookeeper config on each machine
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S1 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S2 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'
ssh -i $KEY_NAME ubuntu@$PUBLIC_IP_S3 'cp -f zookeeper_conf/zookeeper.properties kafka/config/zookeeper.properties'




