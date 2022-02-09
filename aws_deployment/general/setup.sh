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
sudo add-apt-repository -y ppa:openjdk-r/ppa
sudo apt-get update
sudo apt install -y openjdk-11-jdk

# First version used kafka 2.8.1 but I've observed the difference in the checksums so it is probably better to switch version
# 3. Install kafka
#wget https://archive.apache.org/dist/kafka/2.8.1/kafka_2.12-2.8.1.tgz
#sha512sum kafka_2.12-2.8.1.tgz
#tar -xvzf kafka_2.12-2.8.1.tgz
#rm kafka_2.12-2.8.1.tgz
# rename to kafka
#mv kafka_2.12-2.8.1 kafka
wget https://dlcdn.apache.org/kafka/3.1.0/kafka-3.1.0-src.tgz

#VAR="C71477D4 E5CF22B4 DC2E235F 041657ED 5A6EB7D5 394B4783 3BACFAFB E54637FF 54477B73 456849A4 3247819A BEB551ED 4DD25E2D 28163881 E68EB318 9D5A9500"
#CHECKSUM_FROM_THE_WEB=${VAR//[[:blank:]]/}
#CS_LOWER=$(echo $CHECKSUM_FROM_THE_WEB | awk '{print tolower($0)}')
#web=$(sha512sum kafka-3.1.0-src.tgz | awk '{print $1}')
#echo $CS_LOWER
#echo $web
#if [ "$CS_LOWER"!="$web" ]; then
#  echo 'Kafka checksum is not valid'
#  exit 1
#fi
#echo 'Kafka checksum is valid'
tar -xvzf kafka-3.1.0-src.tgz
rm kafka-3.1.0-src.tgz
# rename to kafka
mv kafka-3.1.0-src kafka

# check an exemplary host
#ping kafka3



# Additional setup - swapiness
# Check for additional info: 
# https://medium.com/@ankurrana/things-nobody-will-tell-you-setting-up-a-kafka-cluster-3a7a7fd1c92d
sudo sysctl vm.swappiness=1
echo "vm.swappiness=1" | sudo tee --append /etc/sysctl.conf
