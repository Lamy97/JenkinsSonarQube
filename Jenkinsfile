pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }
  environment {
        APPLICATION_NAME = 'JenkinsSonarQube'
        VERSION = '0.0.1'
        // Module you want to containerize
        APP_MODULE = 'runner-ms'
    }
    parameters {
        string(name: 'NameBranche', defaultValue: 'main', description: 'Nom de la branche à checkout')
    }
    stages {
        stage('Checkout') {
            steps {
                // Checkout explicite vers votre repo GitHub en utilisant le paramètre
                script {
                    git branch: "${params.NameBranche}", url: 'https://github.com/Lamy97/JenkinsSonarQube.git'
                }
                bat 'git rev-parse --abbrev-ref HEAD'
                bat 'git log --oneline -3'

                // Supprimer le remote 'origin' si présent (commande Windows, silencieuse)
                bat 'git remote remove origin 2>nul || echo Origin non present'

                // Debug: afficher les premières lignes du Jenkinsfile (PowerShell Windows)
                bat 'powershell -Command "Get-Content Jenkinsfile | Select-Object -First 140 | ForEach-Object -Begin {$i=1} -Process {\"{0,4}: {1}\" -f $i++, $_ }"'
            }
        }

        stage('Setup') {
            steps {
                // Utiliser WORKSPACE et changer de lecteur si nécessaire
                bat '''
                    echo Java version:
                    java -version
                    echo Maven version:
                    mvn --version
                    echo Working directory:
                    cd /d "%WORKSPACE%"
                    dir
                '''
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat 'mvn sonar:sonar'
                }
            }
        }

        stage('Build & Test') {
            steps {
                // Run tests for all modules
                bat 'mvn -B -U clean test'
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
                bat "mvn -B -U -pl ${APP_MODULE} -am package -DskipTests"

                // Sanity check: show produced jar(s) (Windows)
                bat "if exist ${APP_MODULE}\\\\target (dir ${APP_MODULE}\\\\target) else echo No target folder"
                bat "if exist ${APP_MODULE}\\\\target\\\\*.jar (dir ${APP_MODULE}\\\\target\\\\*.jar) else echo No jar produced"
            }
            post {
                success {
                    // Archive ONLY the runner-ms jar(s)
                    archiveArtifacts artifacts: "${APP_MODULE}/target/*.jar", fingerprint: true
                }
            }
        }


        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
