def call(Map pipelineParams) {
    def dockerImage
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
            stage('docker build') {
                steps {
                    script {
                        dockerImage = docker.build(pipelineParams.dockerImageName, "--force-rm --no-cache .")
                    }
                }
            }
            stage('docker push') {
                steps {
                    script {
                        docker.withRegistry(pipelineParams.dockerRegistry) {
                            dockerImage.push();
                        }
                    }
                }
            }
        }
    }
}