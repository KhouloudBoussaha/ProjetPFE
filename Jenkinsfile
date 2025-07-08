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
                dir('ContinuixV1') { // 📌 Remplace par le bon dossier s’il est différent
                    sh 'mvn clean install'
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
    }
}
