pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build and Test') {
      steps {
        sh 'mvn -B test'
      }
    }

    stage('Publish Test Results') {
      steps {
        junit keepLongStdio: true, testResults: '**/target/surefire-reports/*.xml'
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/surefire-reports/*.xml', allowEmptyArchive: true
    }
  }
}
