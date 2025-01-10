pipeline {
    agent any

    tools {
        jdk 'jdk-21.0.5'
        maven 'Maven 3.3.9'
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
                ng build --prod
                '''
            }
        }

        stage('Compile Backend'){
            steps{
                sh '''
                cd backend
                mvn clean compile
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