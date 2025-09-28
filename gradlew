#!/bin/sh

set -e

APP_HOME="$(cd "$(dirname "$0")" && pwd)"
APP_NAME="Gradle"
APP_BASE_NAME="gradlew"
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ -n "$JAVA_HOME" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
else
  JAVA_CMD="java"
fi

exec "$JAVA_CMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
  -Dorg.gradle.appname=$APP_BASE_NAME \
  -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
