def call(Map pipelineParams) {
    def dockerImage
    pipeline {
        agent any
        stages {
            stage('Node Pacakage Manager Install') {
                steps {
                    nodejs(nodeJSInstallationName: 'NodeJS') {
                        sh 'npm install'
                    }
                }
            }
            stage('Node Pacakage Manager Test') {
                steps {
                    sh 'echo needs to be implemented'
                }
            }
            stage('Node Pacakage Manager Build') {
                steps {
                    nodejs(nodeJSInstallationName: 'NodeJS') {
                        sh 'npm run build:ui'
                    }
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
                            dockerImage = docker.build(pipelineParams.dockerImageName, "--force-rm --no-cache .")
                        } else if (env.BRANCH_NAME == 'dev') {
                            dockerImage = docker.build(pipelineParams.dockerImageName + '-dev', "--force-rm --no-cache .")
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
