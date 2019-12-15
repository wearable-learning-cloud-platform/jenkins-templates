def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('git clone') {
                steps {
                    git(url: pipelineParams.gitUrl, credentialsId: pipelineParams.gitCredentials, branch: pipelineParams.gitBranch)
                }
            }
            stage('build') {
                steps {
                    sh 'mvn -DskipTests package'
                }
            }
            stage('unit test') {
                steps {
                    sh 'mvn test'
                }
            }
            stage('intergration test') {
                steps {
                    sh 'echo still need to be implemented'
                }
            }
        }
    }
}