@echo off
title G3DEditor
java -Xmx512m -Djava.library.path=./lib/ ^
     --add-opens java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens java.desktop/sun.awt=ALL-UNNAMED ^
     --add-opens java.desktop/sun.awt.windows=ALL-UNNAMED ^
     -cp ".\lib\*;G3DEditor.jar" g3deditor.G3DEditor
pause
