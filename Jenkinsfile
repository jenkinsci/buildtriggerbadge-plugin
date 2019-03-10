#!/usr/bin/env groovy


pipeline {
    agent {
        label 'maven'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore --batch-mode --errors -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn'
            }
        }
    }
}
