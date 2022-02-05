# This has to be executed on each machine
#!/bin/bash

# as root check what's there
sudo su
lsblk

# make sure that the disk is empty -> data
file -s /dev/xvdf

# format as xfs
# https://kafka.apache.org/documentation/#filesystems
apt-get install xfsprogs

# create a partition
fdisk /dev/xvdf
# format as xfs
mkfs.xfs -f /dev/xvdf
# create kafka dir
mkdir /var/kafka-logs
# add permissions to kafka dir
chown -R ubuntu:ubuntu /var/kafka-logs
# mount volume
mount -t xfs /dev/xvdf /var/kafka-logs
# check if it works
df -h /var/kafka-logs

# auto mount on restart
cp /etc/fstab /etc/fstab.bak
echo '/dev/xvdf /data/kafka xfs defaults 0 0' >> /etc/fstab
