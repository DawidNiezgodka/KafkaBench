# Launch zookeeper as a daemon
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
# Check that zookeeper works
# Open zookeper props
vim config/zookeeper.properties
# Add the line: 4lw.commands.whitelist=stat, ruok, conf, isro
# Restart zookeeper
# Execute the line:
echo "ruok" | nc localhost 2181 # -> imok

# stop zookeeper
# TODO: check this command for correctness
bin/zookeeper-server-stop.sh


#############################################
#############################################
# CREATING QUORUM
#############################################
#############################################
# Step 1. ssh to each machine separately
# Step 2. execute the following commands on each machine separately

sudo mkdir -p /data/zookeeper
sudo chown -R ubuntu:ubuntu /data/

# Declare the server's id for each machine
# 1 for first machine, 2 for second, 3 for third
echo "1" > /data/zookeeper/myid

# add props, see the file zookeeper-properties in this folder
rm /home/ubuntu/kafka/config/zookeeper.properties
nano /home/ubuntu/kafka/config/zookeeper.properties

# check if it works


