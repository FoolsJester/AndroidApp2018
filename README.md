STUDY BUDDY
===============

This is an android application developed to help students be more productive in their study sessions.
The aim of the application is to act as a social media platform for study. Currently there is a demo user
set up to show the functionality of the app. Within this demo user:
    - Friends can be added
    - Courses can be added and enrolled in
    - Discussion forums can be created and contributed to
    - Study hours can be logged via timer or by direct input
    - Assignments can be added and marked as completed/ incomplete
    
There are several key features within this app, one of which is the ability to track the time and quality
of your study. When a user sets an amount of time in the study timer, the app tracks how much the user
interacts with their phone within that time. It does this by tracking motion through the gyroscope and
by monitoring the state of the screen (on or off). At the end of the allotted time the user's time is
then broken down into productive/ unproductive time. This is then logged on their profile and friends
of the user can then view these statistics in a daily, weekly or monthly breakdown.

The Firebase DB at the back end of this application allows for the generation of dynamic pages. This means
that each page/ activity within can be rendered depending on the user specific data within the firebase.
This is achieved by storing each of the elemets within the app as objects in the DB

===============

Developed by Amy McCormack, Eimear Galligan, Shane Bird & Muireann MacCarthy
