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
                dir('ContinuixV1') {  // Change ça si ton dossier a un autre nom
                    bat 'mvn clean install'
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
