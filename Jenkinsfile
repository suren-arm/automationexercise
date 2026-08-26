/*
 * Jenkins pipeline for exactly TC1, TC9, TC16 and TC25.
 */
pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9'
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser to execute.'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser headlessly.'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile -DskipTests=true'
            }
        }

        stage('Execute TC1 TC9 TC16 TC25') {
            steps {
                /*
                 * Maven's non-zero exit code is preserved so Jenkins marks
                 * the build failed when any automated test fails.
                 */
                sh """
                    mvn -B test \
                      -Dbrowser=${params.BROWSER} \
                      -Dheadless=${params.HEADLESS} \
                      -Denvironment=qa
                """
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            archiveArtifacts(
                artifacts: 'logs/**/*,target/surefire-reports/**/*',
                allowEmptyArchive: true,
                fingerprint: true
            )

            allure([
                results: [[path: 'allure-results']]
            ])
        }
    }
}
