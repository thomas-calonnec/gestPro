pipeline {
    agent any

    stages {

        stage('Clone the repository') {
            steps {
                git url: 'https://github.com/thomas-calonnec/gestPro.git', branch:'main'
            }
        }

        stage('Install dependencies') {
            steps{
                sh '''
                cd frontend
                npm install
                '''
            }
        }

        stage('Run tests'){
            steps{
                sh '''
                cd frontend
                npm test
                '''
            }
        }

        stage('Build') {
            steps{
                sh '''
                cd frontend
                ng build --prod
                '''
            }
        }

    }
}