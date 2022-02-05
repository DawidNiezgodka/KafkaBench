# Having set up the zookeeper quorum and kafka cluster, execute the following commands to test the setup

# 1. ssh on each machine
# 2. start zookeeper and kafka on each machine

# For example, take the first machine and execute:
nc -vz zookeeper1 2181 # -> Connection to zookeeper1 2181 port [tcp/*] succeeded!
nc -vz zookeeper2 2181 # -> Connection to zookeeper2 2181 port [tcp/*] succeeded!
nc -vz zookeeper3 2181 # -> Connection to zookeeper3 2181 port [tcp/*] succeeded!

nc -vz kafka1 9092 # -> Connection to kafka1 9092 port [tcp/*] succeeded!
nc -vz kafka2 9092 # -> Connection to kafka2 9092 port [tcp/*] succeeded!
nc -vz kafka3 9092 # -> Connection to kafka3 9092 port [tcp/*] succeeded!

# Create a topic. From a machine that hosts zookeeper1, choose zookeeper2
bin/kafka-topics.sh --create --zookeeper zookeeper2:2181/kafka --topic topic --partitions 3 --replication-factor 3 # -> Created topic topic.
# Write to a topic, but take third broker that is located on a different machine
bin/kafka-console-producer.sh --broker-list kafka3:9092 --topic topic
# > first msg
# > second msg
# > third msg

# On each machine execute:
bin/kafka-console-consumer.sh --bootstrap-server kafka1:9092 --topic topic --from-beginning

# Each machine should read three messages. Ordering might be different on distinct machines! An exemplary output:
# second msg
# first msg
# third msg