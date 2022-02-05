#!/bin/bash
for ks in ./kafkasettings/*.yml; do
  for ws in ./workloadsettings/*.yml; do
    echo „$ks $ws”
    java -jar benchmark.jar -k “$ks” -w “$ws”
  done
done
