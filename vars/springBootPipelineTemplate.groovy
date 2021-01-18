def call(Map pipelineParams) {
    def dockerImage
    pipeline {
        agent any
        stages {
            stage('Maven Build') {
                steps {
                    sh 'mvn -DskipTests package'
                }
            }
            stage('Run Unit Tests') {
                steps {
                    sh 'mvn test'
                }
            }
            stage('Run Integration Tests') {
                steps {
                    sh 'echo still need to be implemented'
                }
            }
            stage('Docker Build') {
                agent any
                when{
                    branch 'master'
                }
                steps {
                    script {
                        dockerImage = docker.build(pipelineParams.dockerImageName)
                    }
                }
            }
            stage('Docker Push') {
                agent any
                when{
                    branch 'master'
                }
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
