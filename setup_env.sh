#!/bin/bash


###################################Sonar######################
sonar_stop_status="SonarQube is not running."
sonar_status=`sudo /opt/sonarqube/bin/linux-x86-64/sonar.sh status`

if [ "$sonar_stop_status" == "$sonar_status" ]

then
  echo "starting sonar"
sudo /opt/sonarqube/bin/linux-x86-64/sonar.sh start
echo "exposing 9000 port for sonarqube"
sudo iptables -I INPUT -p tcp -m tcp --dport 9000 -j ACCEPT
else
echo "$sonar_status"
fi
###################################Artifactory######################
art_running_status="Artifactory Tomcat stopped"
art_status=`sudo service artifactory status`

if [ "$art_running_status" == "$art_status" ]
then
echo "starting artifactory"
sudo service artifactory start >  /dev/null 2>&1
echo "exposing 8081 port for arifactory"
sudo iptables -I INPUT -p tcp -m tcp --dport 8081 -j ACCEPT
else
echo "$art_status"
fi

###################################Jenkins######################
jenkins_Stop_status="jenkins is stopped"
jenkins_current_status=`sudo service jenkins status`
if [ "$jenkins_current_status" == "$jenkins_Stop_status" ]
then
echo "starting jenkins"
sudo service jenkins start > /dev/null 2>&1
echo "exposing 8081 port for jenkins"
sudo iptables -I INPUT -p tcp -m tcp --dport 8080 -j ACCEPT
else
echo "$jenkins_current_status"
fi