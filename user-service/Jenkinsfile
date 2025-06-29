pipeline {
  agent any

  stages {
    stage('Checkout Code') {
      steps {
        checkout scm
      }
    }

    stage('Build with Jib') {
      steps {
        dir("${params.MICROSERVICE}") {
          sh 'mvn compile jib:build'
        }
      }
    }
  }

  post {
    success {
      echo "✅ ${params.MICROSERVICE} built and pushed successfully!"
    }
    failure {
      echo "❌ Build failed for ${params.MICROSERVICE}"
    }
  }
}
