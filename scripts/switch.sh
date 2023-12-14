##!/bin/bash
#
#CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
#TARGET_PORT=0
#
#echo "> Nginx currently proxies to ${CURRENT_PORT}."
#
#if [ ${CURRENT_PORT} -eq 8080 ]; then
#    TARGET_PORT=8081
#elif [ ${CURRENT_PORT} -eq 8081 ]; then
#    TARGET_PORT=8082
#elif [ ${CURRENT_PORT} -eq 8082 ]; then
#    TARGET_PORT=8080
#else
#    echo "> No WAS is connected to nginx"
#    exit 1
#fi
#
#echo "set \$service_url http://43.201.66.205:${TARGET_PORT};" | tee /home/ec2-user/service_url.inc
