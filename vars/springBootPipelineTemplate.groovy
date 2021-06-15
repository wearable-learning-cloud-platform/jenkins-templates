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
                when{
                    anyOf {
                        branch 'master';
                        branch 'dev'
                    }
                }
                steps {
                    script {
                        if (env.BRANCH_NAME == 'master') {
                            dockerImage = docker.build(pipelineParams.dockerImageName)
                        } else if (env.BRANCH_NAME == 'dev') {
                            dockerImage = docker.build(pipelineParams.dockerImageName + '-dev')
                        }
                    }
                }
            }
            stage('Docker Push') {
                when{
                    anyOf {
                        branch 'master';
                        branch 'dev'
                    }
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
