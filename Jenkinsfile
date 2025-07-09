pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test Maven') {
            steps {
                sh 'mvn -version'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('ContinuixFront-endV1/Continuix') { 
                    sh 'npm install'
                    sh 'ng build --configuration production'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('ContinuixV1') {
                    sh 'mvn clean verify'
                }
            }
        }

    
        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                    dir('ContinuixV1') {
                        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }
    

    post {
        success {
            echo '🎉 Build réussi !'
        }

        failure {
            echo '❌ Échec du pipeline.'
        }

        always {
            // 📄 Rapport des tests JUnit
            junit 'ContinuixV1/target/surefire-reports/*.xml'

            // 📊 Couverture JaCoCo (le plugin Jenkins JaCoCo doit être installé)
            jacoco(execPattern: 'ContinuixV1/target/jacoco.exec')
        }
    }
}
