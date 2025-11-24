@echo off
cd /d "C:\Users\cesit\Desktop\ExoticWorld"

powershell -NoProfile -Command ^
  "$files = @(" ^
  "  'build.gradle.kts','settings.gradle.kts','app\build.gradle.kts'," ^
  "  'app\src\main\AndroidManifest.xml'," ^
  "  'app\src\main\java\com\example\exoticworld\MainActivity.kt'" ^
  ") +" ^
  "(Get-ChildItem app\src\main\java -Recurse -Include *Nav*.kt,*Navigation*.kt,*Screen*.kt,*Activity*.kt,*Fragment*.kt | %% FullName) +" ^
  "(Get-ChildItem app\src\main\res\navigation -Recurse -Include *.xml -ErrorAction SilentlyContinue | %% FullName);" ^
  "'> proyecto_min.txt' | Out-Null;" ^
  "foreach($f in $files){ if(Test-Path $f){ '----- ' + $f + ' -----' | Add-Content proyecto_min.txt; Get-Content $f | Add-Content proyecto_min.txt } }"

echo.
echo Archivo generado: proyecto_min.txt
pause
