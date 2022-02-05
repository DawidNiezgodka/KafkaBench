########
#### ADMIN MACHINE
########


wget https://dl.grafana.com/enterprise/release/grafana-enterprise-8.3.4.linux-amd64.tar.gz
tar -zxvf grafana-enterprise-8.3.4.linux-amd64.tar.gz

# enable anonymous auth
nano conf/defaults.ini

# change the following props in auth.anonymous
# enabled = true (false -> true)
# org_role = Admin (Viewer -> Admin)

# start grafana:
bin/grafana-server

# imports some dashboards

