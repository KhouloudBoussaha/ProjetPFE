pipeline {
    agent any

    stages {
        stage('Testing Maven') {
            steps {
                sh 'mvn -version'
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/KhouloudBoussaha/ProjetPFE.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
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
