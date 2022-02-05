### On each kafka machine
cd /home/ubuntu/kafka/libs
wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.16.1/jmx_prometheus_javaagent-0.16.1.jar
sudo nano /home/ubuntu/kafka/config/sample_exporter.yml
# Copy the content of this config to a newly create file: https://github.com/prometheus/jmx_exporter/blob/master/example_configs/kafka-2_0_0.yml

# Open...
sudo nano /etc/systemd/system/kafka.service
# ...and add the following line there:
# # Add: Environment="KAFKA_OPTS=-javaagent:/home/ubuntu/prometheus/jmx_prometheus_javaagent-0.16.1.jar=7075:/home/ubuntu/prometheus/kafka-2_0_0.yml"
# Restart Kafka
sudo systemctl daemon-reload
sudo systemctl restart kafka
# Check with
# curl kafka1:7075 -> Should return a load of metrics

### On a monitoring machine
wget https://github.com/prometheus/prometheus/releases/download/v2.32.1/prometheus-2.32.1.linux-amd64.tar.gz
tar -xvzf prometheus-2.32.1.tar.gz
mv prometheus-2.32.1.linux-amd64 prometheus
rm prometheus-prometheus-2.32.1.tar.gz
cd prometheus
nano prometheus.yml # -> add props from this folder
./prometheus # run prometheus -> can be checked at IP:9090
# Setup Prometheus SystemD file
sudo nano /etc/systemd/system/prometheus.service