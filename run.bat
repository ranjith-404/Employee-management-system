@echo off
cd /d "C:\Users\Admin\OneDrive\Documents\Default Project\employee-management"
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
"C:\Users\Admin\.m2\wrapper\dists\apache-maven-3.9.16-bin\5grr65jo27hi51sujmtcldfovl\apache-maven-3.9.16\bin\mvn.cmd" spring-boot:run > app.log 2>&1
