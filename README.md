# Mango Student Android Project

## Table of Contents

1. Project Description 
2. Usage
3. Dependencies And Requirements
4. Development and Functionality
5. Copyrights And Acknowledgements

## Project Description 

This project is a collabrative work of six team members as a requirement for the Android Summer Training at FCIS for the year 2022. The project is aimed towards students, it is basically a studies management tool. In this app there is a GPA calculator, Calendar Events Scheduler, Daily To Do List and finally a Pomodoro Technique Timer all of which are combined together in one place to deliver a pleasent user experience. 

## Usage

This app is desgined for students, specifically college students. There are multiple uses for this case, all calibrated to meet the student's need, for instance, you can add a subject and its respective grade and number of credit hours, and the GPA calculator is calculated and displayed automatically. In the Calendar, you can add events with their discriptions and their due time on any date you wish to choose, and on the Daily To Do List, you can view all the to dos you have for the day, add more tasks and delete or edit existing ones. Finally you can study efficiently and neatly in accordance with thescientific Pomodoro Technique which is basically giving yourself sessions of 25 minutes for study time followed by 5 minute breaks.



## Dependencies And Requirements

To run this software, all that is required is an android device with a minimum of android 6.0 Marshmallow installed if you wish to run it on android device.
The code can be viewed/modified on Android Studio with SDK version 33.



- How to run the project?
    open the project using android studio and press on run (the green arrow) to launch the application on the android emulator or a supported android device through usb debugging.

- Technologies used
    - SQLite
    - JAVA
    - XML

-


## Development and Functionality



- Front-End and Design

    This app supports light and dark modes with color profiles that are appropriate for every scenario and sufficient for every user's needs.

    It implements a simplistic design with little to no visual obstacles. The design language focuses on simple, straight forward elements that blend together without causing any visual hazards.

    The design of the app and implementation of a sliding navigation menu, in addition to putting all the small peices together was done by Fady Makram and Mina Adel.

- Back-End and Functionality


    - GPA Calculator

    In this section of the app, the user can add numerous subjects as needed. each subject added is represented by three fields of data, name, number of hours and grade (A to F). On adding a subject the overall GPA is re-calculated with the newly added subject, then it is displayed. There is an option to view a list of all added subjects if neeeded. On long pressing a subject from the list, the user gets the option to either delete the subject or edit any of its three fields. The database is handled through SQLite.

    Implemented by Mina Nabil and Mina Khalifa.

    - Calendar Events Scheduler
    
    The calendar tool is a tool designed to forecast future events and record them in an organized matter to guide the student in the chaotic world of endless events that we live in. In the calendar, you can choose any day, on choosing a day, a counter displays how many events you have on that day. The user can press the "Manage Tasks" buttons to be redirected to the events scheduler window, in this window, all tasks are recorded, you have the option to add a new task or delete/edit an existing one. Long pressing a task views the description of that task. The database is handled through SQLite.

    Implemented by Mena Ashraf.
    
    - Daily To Do List   
    
    This part of the app serves as daily reminder utility that the student can use to keep track of the tasks they have to do today. The To Do List retrieves the daily tasks that are previously recorded in the calendar. it allows the user to view those tasks and check them on completion or delete them, you can also edit those tasks as desired. deleting a task from the Daily To Do list also deletes it from the calendar and vice versa. The database is handled through SQLite.
    
    Implemented by Mina Anis.

    - Pomodoro Technique Timer

    This is a simple counter based on the famous scientific technique that is the Pomodoro technique. The purpose of this part is to regulate the studying process of the student and make it more comfortable and productive, it does so by displaying a 25 minute count down during which the student should study, then a 5 minute counter starts after marking the duration of the break the student gets after each student session. there are options to pause the counter or just reset it all together.

    Implemented by Mina Nabil and Mina Khalifa.



## Copyrights And Acknowledgements

    -All rights reserved for the Mango development team.

