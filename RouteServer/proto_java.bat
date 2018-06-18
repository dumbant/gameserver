
set ProtocalFolderName=proto
set OutProtocalFolderName=E:/work/workspace/gameserver/RouteServer/src
for /f "delims=\" %%a in ('dir /b /a-d /o-d "%ProtocalFolderName%\*.proto"') do (
  echo %ProtocalFolderName%\%%a
  protoc.exe --proto_path=%ProtocalFolderName% --java_out=%OutProtocalFolderName% %ProtocalFolderName%\%%a
)

echo ..
pause & exit /b