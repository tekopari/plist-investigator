@echo off
REM +=======================================================+
REM |                                                       |
REM |  SCRIPT:  PlistNavigator.bat                          |
REM |                                                       |
REM |  PURPOSE: The script sets up the environment          |
REM |           and starts the Plist Navigator              |
REM |                                                       |
REM +=======================================================+

REM
REM The location of the bin directory
REM

SET CLASSPATH=""
"C:\\Program Files\\Java\\jre6\\bin\\java" -cp %CLASSPATH% -jar "C:\\Program Files\\PlistInvestigator\\PlistInvestigator.jar"
