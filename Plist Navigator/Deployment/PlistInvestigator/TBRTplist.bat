@echo off
REM +=======================================================+
REM |                                                       |
REM |  SCRIPT:  TBRTplist.bat                               |
REM |                                                       |
REM |  PURPOSE: The script converts plists between binary   |
REM |           plist format and xml plist format. This is  |
REM |           a command line tool.                        |
REM |                                                       |
REM +=======================================================+

REM
REM The location of the bin directory
REM

SET CLASSPATH=""
"C:\\Program Files\\Java\\jre6\\bin\\java" -cp %CLASSPATH% -jar "C:\\Program Files\\PlistInvestigator\\PlistConvert.jar" %*
