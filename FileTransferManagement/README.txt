#Anantakeat UTAC : Exercise Develop an application ( Java )

Java-based application for file transfer management. Please see the detailed requirements below.

    The submission deadline is **July 18, 2025 at 6:00 PM**.
 

🔧 Functional Requirements:

Monitor Source Folder (Folder A):

    - The application should check for new files in Folder A.

Exclude .temp Files:

    - Any file with the .temp extension must be ignored and not processed.

Compress Files Before Transfer:

    - Each file must be compressed into a .zip file before being transferred to Folder B.

Compare File Size After Compression:

    - After compression, check the size of the .zip file in Folder B.

    - If the size is different from the original file in Folder A, do not transfer the file and keep it in Folder A.

Move Original File to Backup Folder:

    - If the transfer is successful, move the original file from Folder A to a Backup folder.

Run the Application Every 1 Minute:

    - Schedule the application to run automatically every 1 minute (e.g., using a Java timer, scheduled task, or cron job).

Prevent Duplicate Execution:

    - Before starting a new process, check whether the previous instance of the application is still running.

    - If it is, skip the current run to avoid overlapping executions.

 

📦 Deliverables:

Flowchart Diagram:

    - A visual flowchart of the application process in either Excel or PowerPoint format.

Java Source Code:

    - Well-structured and commented source code in Java.

Result Summary:

    - A simple summary of the execution results, e.g., which files were transferred, skipped, or failed (in .txt format).

Event Log Record:

    - A log file that records key events such as start time, end time, files processed, errors encountered, etc. (in .txt format).

Assignment
 - Folder In Drive C Folder
 - Source Code In src Folder

Folder A = Source
 - logo.jpg
 - logo101.jpg
 - result.csv
 - result777.csv
 - test.txt
 
Folder B = Destination
 - logo.jpg.zip
 - logo101.jpg.zip
 - result.csv.zip
 - result777.csv.zip
 - test.txt.zip
 
Result Summary :
 - Logs Folder
	* result-summary.txt
	
Event Log Record :
 - Logs Folder
 	* event-log.txt