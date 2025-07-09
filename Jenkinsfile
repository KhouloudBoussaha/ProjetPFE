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
                dir('ContinuixV1') { // 
                    sh 'mvn clean install'
                }
            }
        }
stage('JaCoCo Coverage Report') {
  steps {
    dir('ContinuixV1') {
      sh 'mvn verify'
    }
  }
}

 stage('sonarqube') {
            steps {
                dir('Sonarqube') { // 
                    sh 'mvn sonar:sonar-Dsonar.login=admin -Dsonar.password=Khouloud793+'
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
