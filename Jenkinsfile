pipeline {
  agent any

  parameters {
    choice(name: 'MICROSERVICE',
           choices: ['discovery-server','api-gateway','userService','notificationService','product-service','order-service',
		   'cart-service','common-dto','inventory-service','loadProduct'],
           description: 'Select the microservice to build')
    
    string(name: 'IMAGE_TAG',
           defaultValue: 'latest',
           description: 'Tag for the Docker image (e.g. v1.0.0)')

    choice(name: 'SPRING_PROFILE',
           choices: ['prod', 'dev'],
           description: 'Spring Boot profile to activate')
  }

  environment {
    REGISTRY = 'docker.io'
    DOCKERHUB_NAMESPACE = 'rishavkumarthakur' 
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build and Push Docker Image') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'dockerhub-creds',
          usernameVariable: 'DOCKERHUB_USER',
          passwordVariable: 'DOCKERHUB_PASS')]) {

          dir("${params.MICROSERVICE}") {
            sh """
              mvn compile jib:build \
                -Djib.to.image=${REGISTRY}/${DOCKERHUB_NAMESPACE}/${params.MICROSERVICE} \
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
      echo "✅ Docker image pushed: ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${params.MICROSERVICE}:${params.IMAGE_TAG}"
    }
    failure {
      echo "❌ Failed to build or push Docker image"
    }
  }
}
