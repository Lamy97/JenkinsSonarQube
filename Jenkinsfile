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
        DOCKER_REPO = 'testblackbird/spring-boot-microservices-tp' // change if needed
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${DOCKER_REPO}:${VERSION}"

        // Module you want to containerize
        APP_MODULE = 'runner-ms'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git rev-parse --abbrev-ref HEAD'
                sh 'git log --oneline -3'
            }
        }

        stage('Setup') {
            steps {
                sh '''
                    set -eux
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

        stage('Build & Test') {
            steps {
                // Run tests for all modules
                sh 'mvn -B -U clean test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                // Package ONLY runner-ms (and build required modules) without rerunning tests
                sh "mvn -B -U -pl ${APP_MODULE} -am package -DskipTests"

                // Sanity check: show produced jar(s)
                sh "ls -lah ${APP_MODULE}/target || true"
                sh "ls -lah ${APP_MODULE}/target/*.jar"
            }
            post {
                success {
                    // Archive ONLY the runner-ms jar(s)
                    archiveArtifacts artifacts: "${APP_MODULE}/target/*.jar", fingerprint: true
                }
            }
        }

        stage('Quality Check') {
            steps {
                // If you want, keep it for all modules:
                sh 'mvn -B checkstyle:check'
            }
        }

        stage('Build Docker Image') {
            when { branch 'main' }
            steps {
                script {
                    // Pick the jar (avoid *sources.jar / *javadoc.jar if they exist)
                    def jarPath = sh(
                      script: "ls -1 ${APP_MODULE}/target/*.jar | grep -vE '(sources|javadoc)\\.jar\\$' | head -n 1",
                      returnStdout: true
                    ).trim()


                    if (!jarPath) {
                        error("No runnable JAR found in ${APP_MODULE}/target")
                    }

                    // Write Dockerfile (simple + reliable)
                    writeFile file: 'Dockerfile', text: """
                    FROM eclipse-temurin:17-jre
                    WORKDIR /app
                    COPY ${jarPath} app.jar
                    EXPOSE 8080
                    ENTRYPOINT ["java","-jar","app.jar"]
                    """.stripIndent()

                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push to Registry') {
            when { branch 'main' }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-registry-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        set -eux
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin docker.io
                        docker push "$DOCKER_IMAGE"
                    '''
                }
            }
        }

        stage('Deploy') {
            when { branch 'main' }
            steps {
                echo 'Deploying application...'
                // sh 'kubectl apply -f k8s-deployment.yaml'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        cleanup {
            cleanWs()
        }
    }
}
