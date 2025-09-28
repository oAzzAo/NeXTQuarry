@ECHO OFF

SET DIR=%~dp0
IF "%DIR%"=="" SET DIR=.
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIR%

SET DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

IF DEFINED JAVA_HOME (
  SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
  IF EXIST "%JAVA_EXE%" GOTO execute
  ECHO ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
  GOTO fail
) ELSE (
  SET JAVA_EXE=java.exe
  WHERE java >NUL 2>NUL
  IF ERRORLEVEL 1 (
    ECHO ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
    GOTO fail
  )
)

:execute
SET CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
GOTO end

:fail
EXIT /B 1

:end
EXIT /B 0
