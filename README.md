# news
Take-home Exercise
Android Engineer - Resolve to Save Lives – Digital
Team

Overview
Simple is an offline-first application. It stands out from other Android apps in that
it handles large amounts of data locally on device. It fetches data over the network,
caches locally in a SQL database, and operates off of local data. Hence, this
take-home exercise and its evaluation rubric reflect an extra emphasis on working
with a database.
Please read the problem statement, requirements, and evaluation criteria carefully
before starting the exercise. If you have any questions, reach out to us to clarify
them before you get started, or along the way. It’s always better to clarify these
things early!
Please spend 3-4 hours to complete this exercise. You can work on your own
computer in an environment that is comfortable to you. You are free to use the
internet to Google things, just as you would on a normal work day. Being
considerate of your time, we ask that the exercise is submitted no more than one
week after it is provided to you.
Good luck!

Problem Statement
Create a newsreader Android application using Kotlin. The app should list news
articles with headlines and brief descriptions, link users to the full article when
clicked, and provide a search function to search for news articles by content or
author.

Requirements
● Use git, commit code as you go, and work iteratively. Once completed, make
your git repository public and share with us as your submission.
● Use the News API (https://newsapi.org/) to retrieve news articles. You will
have to obtain your own API key from the News API.
● Retrieved news articles must be stored in an SQL database on device.
● News articles must be displayed from the local SQL database (not from
network) on the app’s home screen.
● The app should display top headlines
(https://newsapi.org/docs/endpoints/top-headlines) from India on the home
screen.
● When the search is used, the app should fetch and display matching results
from all top headlines, not just India.
● When the search is used, the search results should be stored in the local
database, and rendered on screen from the database.
● When any news article is clicked, the user should be redirected to the full
article.
● Write automated tests to cover the business logic and functionality of the
application.

Evaluation Criteria
Your submission will be evaluated on the following criteria
● Does the application achieve the specifications above?
● Does the code demonstrate separation of concerns between business logic,
UI, and other elements?
● Is the application covered by automated tests?
● Does the application present a polished UI with concern of the user
experience?
