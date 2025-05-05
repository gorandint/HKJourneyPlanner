== Versions ==

For details, please refer to the release summary.

planner-1.0.jar: 2024-10-15
planner-2.0.jar: 2024-11-07
planner-latest.jar (v3.4): 2024-11-25

== How to run the code ==

Java SE 11 or later is required to run the code.

Please execute the following commands in the /release directory, where the .jar file is located.

Platform-independent: java -jar planner-latest.jar

Windows: run-planner-latest.bat (You can also double-click on the .bat file)

Linux: run-planner-latest.sh (You may need to run chmod +x run-planner.sh first)

== Troubleshooting ==

If the above methods do not work, you can run the code using the following command:

Windows: java -cp "planner-latest.jar;lib/*" org.mojimoon.planner.Main

Linux: java -cp "planner-latest.jar:lib/*" org.mojimoon.planner.Main
