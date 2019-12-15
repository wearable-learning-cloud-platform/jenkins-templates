def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('git clone') {
                steps {
                    git(url: pipelineParams.gitUrl, credentialsId: pipelineParams.gitCredentials, branch: pipelineParams.gitBranch)
                }
            }
            stage('npm install') {
                steps {
                    sh 'npm install'
                }
            }
            stage('npm test') {
                steps {
                    sh 'npm test'
                }
            }
            stage('npm lint') {
                steps {
                    sh 'echo need to implement'
                }
            }
            stage('npm build') {
                steps {
                    sh 'npm run build:ui'
                }
            }
        }
    }
}