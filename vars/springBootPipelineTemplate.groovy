def call(Map pipelineParams) {
    def dockerImage
    pipeline {
        agent any
        stages {
            stage('Maven Build') {
                steps {
                	script {
                		if(pipelineParams.containsKey("SSL")) {
                            sh 'rm -rf target/classes/keystore'
                			sh 'rm -rf src/main/resources/keystore'
                			sh 'mkdir src/main/resources/keystore'
                			withCredentials([file(credentialsId: pipelineParams.SSL, variable: 'FILE')]) {
                				sh 'cp $FILE src/main/resources/keystore'
                			}
                		}	
                	}
                    withMaven(maven: 'Maven')  {
                        sh 'mvn -DskipTests package'
                    }
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
