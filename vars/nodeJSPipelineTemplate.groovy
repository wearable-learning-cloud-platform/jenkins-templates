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
                    sh 'echo needs to be implemented'
                }
            }
            stage('npm build') {
                steps {
                    sh 'npm run build:ui'
                }
            }
            stage('docker build') {
                steps {
                    script {
                        docker.build(pipelineParams.dockerImageName, "--force-rm --no-cache .")
                    }
                }
            }
        }
    }
}