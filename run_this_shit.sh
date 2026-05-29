#! /bin/bash
rm -rf bin/ out/
javac -d out src/*.java
_JAVA_AWT_WM_NONREPARENTING=1 java -cp out Main

