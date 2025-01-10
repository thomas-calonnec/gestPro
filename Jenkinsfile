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
                sh 'npm install'
            }
        }

        stage('Run tests'){
            steps{
                sh 'npm test'
            }
        }

        stage('Build') {
            steps{
                sh 'ng build --prod'
            }
        }

    }
}