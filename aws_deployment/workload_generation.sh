#### WORKLOAD GENERATOR
# Assumption: a S3 bucket has already been created and a JAR file and {kafka, workload}settings have 
# already been uploaded


AZ3="eu-central-1c"
CIDR_AZ3="172.31.0.0/20"
SUBNET_AZ3=$(aws ec2 describe-subnets --filter Name=availability-zone,Values=eu-central-1c --output text --query "Subnets[].SubnetId")
UBUNTU_IMAGE="ami-042ad9eec03638628"
KEY_NAME="keyname"
WORKLOAD_MACHINE="t2.xlarge"
REGION="eu-central-1"

# create a new security group and get its id
SEC_GROUP_ID=$(aws ec2 create-security-group --group-name workload --vpc-id $VPC_ID --description "Security group for workload generator" --output text --query "GroupId")
SG_ACCESSOR="sg-"
SEC_GROUP_NAME=$(aws ec2 describe-security-groups --filter Name=group-id,Values=$SEC_GROUP_ID --output text --query "SecurityGroups[].GroupName")

# configure security groups
# ssh from local machine
aws ec2 authorize-security-group-ingress --group-id $SEC_GROUP_ID --protocol tcp --port 22 --cidr $MY_IP/32

# get files from S3 bucket onto the machine
aws s3 sync s3://bench-jars/bench/ /home/ubuntu/benchaws

# get the updated jar
aws s3 cp s3://bench-jars/bench/benchaws/benchmark.jar benchmark.jar

# Then I didn't manage to run the benchmark automatically so I had to do this manually, for example.
java -jar benchmark.jar -k kafkaP100/A0_B524288_PART100.yml -w workloadP100/p1r50k.yml
# this was done for all possible combinations in the kafkasettings+workloadsettings and than kafkaP100+workloadP100 folders


# finally send the results of all benchmark runs to the S3 bucket from where it can be downloaded
# note, in the data analysis part I change the name of this file to PART1_rawResults.csv
aws s3 cp testFolder/results.csv s3://bench-jars/bench/benchaws/