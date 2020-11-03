pipeline {
    agent {
        docker {
            image 'fintech/base-agent'
            args '--network host -e DOCKER_HOST=tcp://localhost:2375'
        }
    }
    options {
        ansiColor('xterm')
        timestamps()
    }
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'openjdk:11-jdk'
                    reuseNode true
                }
            }
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package surefire-report:report-only'
            }
            post {
                always {
                    script {
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                }
            }
        }
        stage('Sonar') {
            when { branch 'master' }
            agent {
                docker {
                    image 'fintech/sonar-agent'
                    reuseNode true
                }
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        sh "sonar-scanner -Dsonar.projectKey=kale-team::solid-lendig-platform -Dsonar.java.binaries=./target/classes"
                    }
                }
            }
        }
        stage('Docker push') {
            when { branch 'master' }
            agent {
                docker {
                    image 'fintech/base-agent'
                    reuseNode true
                    args '--network host -e DOCKER_HOST=tcp://localhost:2375'
                }
            }
            steps {
                script {
                    docker.withRegistry('https://kale-team-docker-registry.fintechchallenge.pl/v2/', 'docker-push-user') {
                        def build = docker.build("kale-team/solid-lendig-platform")
                        build.push("latest")
                    }
                }
            }
        }
        stage('Deploy Sit') {
            when { branch 'master' }
            agent {
                docker {
                    image 'fintech/kubernetes-agent'
                    reuseNode true
                }
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig-sit', variable: 'KUBECONFIG')]) {
                        sh "kubectl apply -f ./kubernetes-sit.yaml"
                        sh "kubectl rollout restart deployment solid-lendig-platform"
                        sh "kubectl rollout status deployment solid-lendig-platform --timeout=3m"
                    }
                }
            }
        }
        stage('Deploy Uat') {
            when { branch 'master' }
            agent {
                docker {
                    image 'fintech/kubernetes-agent'
                    reuseNode true
                }
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig-uat', variable: 'KUBECONFIG')]) {
                        sh "kubectl apply -f ./kubernetes-uat.yaml"
                        sh "kubectl rollout restart deployment solid-lendig-platform"
                        sh "kubectl rollout status deployment solid-lendig-platform --timeout=3m"
                    }
                }
            }
        }
    }
}