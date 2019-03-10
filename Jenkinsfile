#!/usr/bin/env groovy


pipeline {
    agent none
    stages {
        stage('Build JDK8') {
            agent { label 'maven' }

            steps {
                sh 'mvn -Dmaven.test.failure.ignore --batch-mode --errors -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn clean install'
            }
        }
        stage('Build JDK11') {
            agent { label 'maven-11' }

            steps {
                sh 'mvn -Dmaven.test.failure.ignore --batch-mode --errors -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn clean install'
            }
        }
    }
}
