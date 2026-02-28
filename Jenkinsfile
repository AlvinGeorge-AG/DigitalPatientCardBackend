pipeline {
  agent any
  environment {
    NEXUS_DOCKER_REGISTRY='192.168.1.10:8081'
    IMAGE_NAME='digital-patient-card'
    DB_URL='jdbc:postgresql://ep-proud-math-aisc7pll.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require'
  }
  stages {
    stage('Checkout'){
      steps {
        echo 'Downloading Source Code From GitHUB....'
        checkout scm
      }
    }

    stage('Testing'){
      steps {
        echo 'Testing the Code.....'
        withCredentials(
          [usernamePassword(credentialsId:'neondb' , usernameVariable:'DB_USERNAME' , passwordVariable:'DB_PASSWORD')]
        ){
          sh """
              echo "DB_PASSWORD=${DB_PASSWORD}" > .env
              echo "DB_USERNAME=${DB_USERNAME}" >> .env
              echo "DB_URL=${DB_URL}" >> .env
          """
          sh './mvnw clean test'
        }
      }
      post {
          always {
              sh 'rm -f .env'
          }
      }
    }
    
    stage('Build Artifact'){
      steps {
        echo 'Building .jar ....'
        sh './mvnw clean package -DskipTests'
      }
    }

    stage('Build Image'){
      steps {
        echo 'Building Docker Image...'
        sh "docker build -t ${NEXUS_DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} ."
      }
    }

    stage('Push'){
      steps {
        echo 'Pushing image to NEXUS....'
        withCredentials(
          [usernamePassword(credentialsId: 'nexus-creds', passwordVariable: 'NEXUS_PASS', usernameVariable: 'NEXUS_USER')]
        ){
            sh "docker login ${NEXUS_DOCKER_REGISTRY} -u ${NEXUS_USER} -p ${NEXUS_PASS}"

            sh "docker push ${NEXUS_DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"

            sh "docker rmi ${NEXUS_DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
        }
      }
    }
  }
  post {
        success {
            echo "SUCCESS! Docker Image pushed to Nexus."
        }
        failure {
            echo "FAILED! Check the Jenkins Console Output."
        }
    }
}
