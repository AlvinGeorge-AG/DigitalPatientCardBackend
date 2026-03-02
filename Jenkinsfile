pipeline {
    agent any

    environment {
        IMAGE_NAME = 'digital-patient-card'
        AWS_REGION = 'ap-south-1'
        DB_URL = 'jdbc:postgresql://ep-proud-math-aisc7pll.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Cloning source code...'
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo 'Building JAR file...'
                sh '''
                    chmod +x mvnw
                    ./mvnw versions:set -DremoveSnapshot -DgenerateBackupPoms=false
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-account-id', variable: 'AWS_ACCOUNT_ID')
                ]) {
                    script {
                        env.ECR_REPO = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
                    }

                    sh '''
                        docker build -t $ECR_REPO:$BUILD_NUMBER .
                    '''
                }
            }
        }

        stage('Push To AWS ECR') {
            steps {
                echo 'Pushing image to AWS ECR...'

                withCredentials([
                    string(credentialsId: 'aws-account-id', variable: 'AWS_ACCOUNT_ID'),
                    [$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']
                ]) {

                    script {
                        env.ECR_REPO = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
                    }

                    sh '''
                        aws ecr get-login-password --region $AWS_REGION \
                        | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                        docker push $ECR_REPO:$BUILD_NUMBER

                        docker rmi -f $ECR_REPO:$BUILD_NUMBER
                    '''
                }
            }
        }

        stage('Deploy To EC2') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-account-id', variable: 'AWS_ACCOUNT_ID'),
                    string(credentialsId: 'ec2-public-ip', variable: 'EC2_PUBLIC_IP'),
                    [$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']
                ]) {

                    sshagent(['ec2-ssh-key']) {

                        script {
                            env.ECR_REPO = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
                        }

                        sh '''
                            ssh -o StrictHostKeyChecking=no ubuntu@$EC2_PUBLIC_IP "
                                aws ecr get-login-password --region $AWS_REGION \
                                | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                                sudo docker rm -f patient-card-app || true
                                sudo docker pull $ECR_REPO:$BUILD_NUMBER
                                sudo docker run -d -p 80:8080 --name patient-card-app $ECR_REPO:$BUILD_NUMBER
                            "
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS! Application built, pushed to ECR, and deployed."
        }
        failure {
            echo "FAILED! Check Jenkins console output."
        }
    }
}
