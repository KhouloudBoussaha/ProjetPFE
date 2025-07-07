pipeline {
    agent any

    stages {
        stage('Testing Maven') {
            steps {
                bat 'mvn -version'
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
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
