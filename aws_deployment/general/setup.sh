# 1. Install necessary packages
sudo apt-get update && sudo apt-get -y install \
wget \
ca-certificates \
zip \
net-tools \
vim \
nano \
tar \
netcat

# 2. Install java
# 2.1 It might be necessary to add the repo
sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt-get update
sudo apt install openjdk-11-jdk

# 3. Install kafka
wget https://archive.apache.org/dist/kafka/2.8.1/kafka_2.12-2.8.1.tgz
sha512sum kafka_2.12-2.8.1.tgz
tar -xvzf kafka_2.12-2.8.1.tgz
rm kafka_2.12-2.8.1.tgz
# rename to kafka
mv kafka_2.12-2.8.1 kafka

# 4. Mock DNS
# We have three subnets in the VPC
# In each subnet there is one kafka - zookeeper pair
# First subnet is given by 172.31.0.0/20
# Second by 172.31.16.0/20
# Third by 172.31.32.0/20
# Thus, we have to choose an IP for each pair in the corresponding range
echo "
172.31.11.1 kafka1
172.31.11.1 zookeeper1
172.31.22.2 kafka2
172.31.22.2 zookeeper2
172.31.33.3 kafka3
172.31.33.3 zookeeper3
" | sudo tee -a /etc/hosts

# check an exemplary host
ping kafka3



# Additional setup - swapiness
# Check for additional info: 
# https://medium.com/@ankurrana/things-nobody-will-tell-you-setting-up-a-kafka-cluster-3a7a7fd1c92d
sudo sysctl vm.swappiness=1
echo "vm.swappiness=1" | sudo tee --append /etc/sysctl.conf
