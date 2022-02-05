### Execute on each machine
#!/bin/bash

# edit kafka config -> copy the content of server.properties
nano config/server.properties

# Add file limits configs, recommendation from: https://dattell.com/data-architecture-blog/apache-kafka-optimization/#:~:text=File%20descriptor%20limits%3A%20Kafka%20uses,data%20transfer%20between%20data%20centers.
echo "* hard nofile 100000
* soft nofile 100000" | sudo tee --append /etc/security/limits.conf