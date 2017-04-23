#!/usr/bin/env bash

MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m -XshowSettings:vm"

GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark"

GC_LOG=" -verbose:gc -Xloggc:./logs/gc_pid_%p.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"

DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

mvn clean package

java $MEMORY $GC $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/gcTest.jar > ./logs/jvm.out