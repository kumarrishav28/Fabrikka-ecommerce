pipeline {
  agent any

  // Define required tools
  tools {
    maven 'MAVEN' // Name as configured in Jenkins Global Tool Configuration
  }

  // Pipeline parameters for flexibility
  parameters {
    choice(
      name: 'MICROSERVICE',
      choices: [
        'discovery-server', 'api-gateway', 'user-service', 'notification-service',
        'product-service', 'order-service', 'cart-service', 'common-dto',
        'inventory-service', 'load-product'
      ],
      description: 'Select the microservice to build'
    )
    string(
      name: 'DOCKERHUB_NAMESPACE',
      defaultValue: 'your-dockerhub-namespace',
      description: 'DockerHub namespace'
    )
    string(
      name: 'IMAGE_TAG',
      defaultValue: 'latest',
      description: 'Tag for the Docker image (e.g. v1.0.0)'
    )
    choice(
      name: 'SPRING_PROFILE',
      choices: ['prod', 'dev'],
      description: 'Spring Boot profile to activate'
    )
  }

  // Set environment variables
  environment {
    REGISTRY = 'docker.io'
    DOCKERHUB_NAMESPACE = "${params.DOCKERHUB_NAMESPACE}"
  }

  stages {
    stage('Checkout') {
      steps {
        // Clean workspace and checkout code
        deleteDir()
        checkout scm
      }
    }

    stage('Build and Push Docker Image') {
      steps {
        // Use credentials for GitHub and DockerHub
        withCredentials([
          usernamePassword(
            credentialsId: 'github-creds',
            usernameVariable: 'GITHUB_USERNAME',
            passwordVariable: 'GITHUB_TOKEN'
          ),
          usernamePassword(
            credentialsId: 'dockerhub-creds',
            usernameVariable: 'DOCKERHUB_USER',
            passwordVariable: 'DOCKERHUB_PASS'
          )
        ]) {
          // Generate Maven settings.xml for GitHub authentication
          script {
            writeFile file: 'settings.xml', text: """
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>${GITHUB_USERNAME}</username>
      <password>${GITHUB_TOKEN}</password>
    </server>
  </servers>
</settings>
"""
          }

          // Build and push Docker image using Jib
          dir("${params.MICROSERVICE}") {
            sh """
            echo "Building and pushing Docker image for ${params.MICROSERVICE} with tag ${params.IMAGE_TAG}"
              mvn compile jib:build -s ../settings.xml \
                -Djib.to.image=${REGISTRY}/${DOCKERHUB_NAMESPACE}/${params.MICROSERVICE}:${params.IMAGE_TAG} \
                -Djib.to.tags=${params.IMAGE_TAG} \
                -Djib.to.auth.username=${DOCKERHUB_USER} \
                -Djib.to.auth.password=${DOCKERHUB_PASS} \
                -Dspring.profiles.active=${params.SPRING_PROFILE}
            """
          }
        }
      }
    }
  }

  post {
    success {
      // Notify on success
      echo "✅ Docker image pushed: ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${params.MICROSERVICE}:${params.IMAGE_TAG}"
    }
    failure {
      // Notify on failure
      echo "❌ Failed to build or push Docker image"
    }
  }
}
