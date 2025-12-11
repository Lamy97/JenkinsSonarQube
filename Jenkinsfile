pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'jdk-17'
    }

    environment {
        APPLICATION_NAME = 'spring-boot-microservices-tp'
        VERSION = '0.0.1'
        DOCKER_REGISTRY = 'docker.io'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git branch'
                sh 'git log --oneline -3'
            }
        }

        stage('Setup') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    mvn --version
                    echo "Working directory:"
                    pwd
                    ls -la
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts '**/target/*.jar'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Quality Check') {
            steps {
                sh 'mvn checkstyle:check'
                // Add other quality gates like SonarQube if needed
            }
        }

        stage('Build Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Create Dockerfile if not exists
                    def dockerfileContent = """
                    FROM openjdk:17-jdk-slim
                    COPY runner-ms/target/*.jar app.jar
                    EXPOSE 8080
                    ENTRYPOINT ["java", "-jar", "/app.jar"]
                    """
                    writeFile file: 'Dockerfile', text: dockerfileContent

                    // Build Docker image
                    sh "docker build -t ${APPLICATION_NAME}:${VERSION} ."
                    sh "docker tag ${APPLICATION_NAME}:${VERSION} ${DOCKER_REGISTRY}/${APPLICATION_NAME}:${VERSION}"
                }
            }
        }

        stage('Push to Registry') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Login to Docker registry
                    withCredentials([usernamePassword(
                        credentialsId: 'docker-registry-creds',
                        usernameVariable: 'testblackbird',
                        passwordVariable: "yr,.~dYw*H9wbU'"
                    )]) {
                        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin ${DOCKER_REGISTRY}"
                        sh "docker push ${DOCKER_REGISTRY}/${APPLICATION_NAME}:${VERSION}"
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying application...'
                // Add deployment steps (Kubernetes, AWS, etc.)
                // Example for Kubernetes:
                // sh 'kubectl apply -f k8s-deployment.yaml'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
            // emailext (
            //     subject: "SUCCESS: Pipeline ${currentBuild.fullDisplayName}",
            //     body: "Build ${env.BUILD_URL} completed successfully!",
            //     to: 'team@example.com'
            // )
        }
        failure {
            echo 'Pipeline failed!'
            // emailext (
            //     subject: "FAILURE: Pipeline ${currentBuild.fullDisplayName}",
            //     body: "Build ${env.BUILD_URL} failed! Check console output.",
            //     to: 'team@example.com'
            // )
        }
        cleanup {
            cleanWs()  // Clean workspace after build
        }
    }
}