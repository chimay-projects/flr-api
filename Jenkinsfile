pipeline {
  agent {
    node {
      label 'maven'
    }
  }
  options {
    timeout(time: 20, unit: 'MINUTES')
  }
  stages {
    stage('Create Image Builder') {
      when {
        expression {
          openshift.withCluster() {
              openshift.withProject("development-flr") {
                return !openshift.selector("bc", "api").exists()
              }
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject("development-flr") {
              openshift.newBuild("--name=api", "--image-stream=openshift/jdk8", "--binary")
            }
          }
        }
      }
    }
    stage('build') {
      steps {
        script {
            openshift.withCluster() {
                sh "./gradlew build"
                openshift.withProject("development-flr") {
                  openshift.selector("bc", "api").startBuild("--from-file=build/libs/app.jar", "--wait")
                }
            }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject("development-flr") {
              openshift.tag("api:latest", "api:dev")
            }
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            openshift.withProject("development-flr") {
              return !openshift.selector('dc', 'api').exists()
            }
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject("development-flr") {
              openshift.newApp("api:dev", "--name=api").narrow('svc').expose()
            }
          }
        }
      }
    }
  }
}
