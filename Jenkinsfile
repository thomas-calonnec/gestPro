pipeline {
    agent any

    tools {
        jdk 'openjdk 21.0.5'
        maven 'maven'
    }
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

        /*stage('Run tests'){
            steps{
                sh '''
                cd frontend
                npm test
                '''
            }
        }*/

        stage('Build') {
            steps{
                sh '''
                cd frontend
                ng build --configuration production
                '''
            }
        }

        stage('Clean and install Backend'){
            steps{
                sh '''
                cd backend
                mvn clean install
                '''
            }
        }

        stage('Run tests') {
            steps {
                sh '''
                    cd backend
                    mvn test
                    '''
            }
        }

        stage('Build package'){
            steps {
                sh '''
                    cd backend
                    mvn package
                '''
            }
        }

    }
}