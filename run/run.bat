:: Run app!

@echo off
setlocal

:: Capture script directory.
set script_dir=%~dp0

:: Capture project to run.
set project=%1

:: Capture arguments.
set args=
shift
:capture_args
if not "%~1"=="" set "args=%args% %~1" & shift & goto :capture_args

:: Build and get executable.
set "gpx=%script_dir%gpx.jar"
for /f %%i in ('java -jar %gpx% code %project%') do set executable=%%i

:: Run app.
call %executable%%args%

:: Exit.
exit /b
