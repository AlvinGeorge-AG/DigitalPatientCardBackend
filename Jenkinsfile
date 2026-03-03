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
                    sh '''
                        # Construct the URL securely in Bash
                        ECR_REPO="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
                        docker build -t $ECR_REPO:$BUILD_NUMBER .
                    '''
                }
            }
        }

        stage('Push To AWS ECR') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-account-id', variable: 'AWS_ACCOUNT_ID'),
                    aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    sh '''
                        ECR_REPO="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
        
                        # 1. Spin up AWS CLI container for 1 second just to get the password
                        # 2. Pass our Jenkins AWS credentials into it
                        # 3. Pipe (|) the output directly into Jenkins' Docker login
                        docker run --rm \
                            -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
                            -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
                            amazon/aws-cli:latest \
                            ecr get-login-password --region $AWS_REGION \
                        | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
        
                        # Push the image and clean up the local server to save disk space
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
                    aws(credentialsId: 'aws-creds', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    sshagent(['ec2-ssh-key']) {
                        sh '''
                            ECR_REPO="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"

                            # We SSH into EC2. The double quotes (") allow our Jenkins variables to be injected.
                            ssh -o StrictHostKeyChecking=no ubuntu@$EC2_PUBLIC_IP "
                                
                                # We use the exact same trick on the EC2 server! 
                                # EC2 temporarily pulls the AWS CLI container to securely log into ECR
                                sudo docker run --rm \
                                    -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
                                    -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
                                    amazon/aws-cli:latest \
                                    ecr get-login-password --region $AWS_REGION \
                                | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                                # Clean up old app, pull the new one, and deploy
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
