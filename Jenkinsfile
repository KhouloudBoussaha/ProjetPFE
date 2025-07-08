pipeline {
    agent any

    stages {
        stage('Testing Maven') {
            steps {
                sh 'mvn -version'
            }
        }
stage('Build Frontend') {
  steps {
    dir('frontend') {
      sh 'npm install'
      sh 'ng build --configuration production'
    }
  }
}

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('ContinuixV1') {  // Adapte ce chemin si ton projet a un autre nom
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
