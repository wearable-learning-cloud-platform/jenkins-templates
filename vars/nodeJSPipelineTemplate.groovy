def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('git clone') {
                steps {
                    git(url: pipelineParams.gitUrl, credentialsId: pipelineParams.gitCredentials, branch: pipelineParams.gitBranch)
                }
            }
        }
    }
}