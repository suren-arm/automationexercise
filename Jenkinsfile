/*
 * Jenkins pipeline for exactly TC1, TC9, TC16 and TC25.
 */
pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9'
    }

    /*
     * All parameters default to empty on purpose.
     *
     * config.properties is the single source of truth for how the suite runs,
     * and a blank value is ignored by ConfigReader - so an unattended build
     * behaves exactly like `mvn clean test` on a developer machine. Filling one
     * in overrides the file for that build only, which is what makes an ad-hoc
     * "run it on Firefox once" possible without a commit.
     *
     * A choice/boolean parameter cannot express this: it always resolves to a
     * value, so it would silently override config.properties on every build and
     * CI would quietly stop matching local runs.
     */
    parameters {
        string(
            name: 'BROWSER',
            defaultValue: '',
            description: 'chrome, firefox/gecko or edge. Empty = use config.properties.'
        )

        string(
            name: 'HEADLESS',
            defaultValue: '',
            description: 'true or false. Empty = use config.properties.'
        )

        string(
            name: 'LOG_LEVEL',
            defaultValue: '',
            description: 'TRACE, DEBUG, INFO, WARN or ERROR. Empty = use config.properties.'
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
                 * Blank parameters are forwarded as blank and ignored by
                 * ConfigReader, leaving config.properties in charge. Maven's
                 * non-zero exit code is preserved so Jenkins marks the build
                 * failed when any automated test fails.
                 */
                sh """
                    mvn -B test \
                      -Dbrowser='${params.BROWSER}' \
                      -Dheadless='${params.HEADLESS}' \
                      -DLOG_LEVEL='${params.LOG_LEVEL}'
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
