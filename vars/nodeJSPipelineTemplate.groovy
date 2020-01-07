def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('Node Pacakage Manager Install') {
                steps {
                    sh 'npm install'
                }
            }
            stage('Node Pacakage Manager Test') {
                steps {
                    sh 'echo needs to be implemented'
                }
            }
            stage('Node Pacakage Manager Build') {
                steps {
                    sh 'npm run build:ui'
                }
            }
            stage('Docker Build') {
                steps {
                    script {
                        docker.build(pipelineParams.dockerImageName, "--force-rm --no-cache .")
                    }
                }
            }
            stage('Docker Push') {
                steps {
                    script {
                        docker.withRegistry(pipelineParams.dockerRegistry, pipelineParams.dockerRegistryCredential) {
                            dockerImage.push();
                        }
                    }
                }
            }
        }
    }
}
