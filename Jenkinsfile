pipeline {
  agent any
  environment {
    NEXUS_DOCKER_REGISTRY='10.220.136.82:8083'
    IMAGE_NAME='digital-patient-card'
    DB_URL='jdbc:postgresql://ep-proud-math-aisc7pll.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require'
    AWS_REGION="ap-south-1"
  }
  stages {
    stage('Checkout Codes'){
      steps {
        echo 'Downloading Source Code From GitHUB....'
        checkout scm
      }
    }
    
    stage('Build Artifact'){
      steps {
        echo 'Building .jar ....'
        sh 'chmod +x mvnw'
        sh './mvnw versions:set -DremoveSnapshot -DgenerateBackupPoms=false'
        sh './mvnw clean package -DskipTests'
      }
    }

    stage('Build Image'){
      steps {
        withCredentials(
          [string(credentialsId: 'aws-account-id', variable: 'AWS_ACCOUNT_ID')]
        ){
            echo 'Building Docker Image...'
            script {
              env.ECR_REPO="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/digital-patient-card"
            }
            sh "docker build -t ${env.ECR_REPO}:${BUILD_NUMBER} ."
        }
      }
    }

    stage('Push'){
      steps {
        echo 'Pushing image to NEXUS....'
        withCredentials(
          [
                    string(credentialsId: 'ec2-public-ip', variable: 'EC2_PUBLIC_IP'),
                    aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')
          ]
        ){
            // sh "docker login ${NEXUS_DOCKER_REGISTRY} -u ${NEXUS_USER} -p ${NEXUS_PASS}"
            sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
            // sh "docker push ${NEXUS_DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
            sh "docker push ${env.ECR_REPO}:${BUILD_NUMBER}"
          
            echo "Removing the Docker Image from Memory..."
            sh "docker rmi -f ${env.ECR_REPO}:${BUILD_NUMBER}"
        }
      }
    }

    stage('Deploy to EC2'){
      steps{
        sshagent(['ec2-ssh-key']){
          sh "ssh -o StrictHostKeyChecking=no ubuntu@${EC2_PUBLIC_IP} 'aws ecr get-login-password --region ${AWS_REGION} | sudo docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com'"

          sh """
                  ssh ubuntu@${EC2_PUBLIC_IP} 
                     'sudo docker rm -f patient-card-app || true
                      sudo docker pull ${env.ECR_REPO}:${BUILD_NUMBER}
                      sudo docker run -d -p 80:8080 --name patient-card-app ${env.ECR_REPO}:${BUILD_NUMBER}
                     '
              """
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
