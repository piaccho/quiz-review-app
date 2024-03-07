

<h1 align="center" id="title">Quiz Review App 🦄</h1>

<p id="description">This project is a desktop application built as a part of Object-oriented technologies course at AGH University of Krakow. It allows for importing quiz results in form of Microsoft Forms xlsx output file and displaying them in a user-friendly format and assigning rewards based on configurable strategies.</p>

## 📖 Table of contents

* [Overview](#overview)
* [Used technologies](#used-technologies)
* [Features](#features)
* [Setup](#setup)
* [Example of use](#example-of-use)
* [Credits](#credits)

<div id="overview" />

## 🔎 Overview  
This application simplifies managing quiz results by providing tools for:

* Importing quiz data from xlsx files.
* Visualizing results in a sortable table with essential details (pet name, correct answers, timestamp, prize).
* Defining and assigning rewards based on user-defined strategies.
* Exporting results with assigned awards to xlsx and pdf formats.
* Displaying comprehensive quiz statistics for analyzing performance.

<div id="used-technologies" />

## ⚙️ Used technologies

* Programming Language: <b>Java 17</b>
* Frontend Framework: <b>JavaFX 17</b>
* Backend Framework: <b>Spring Boot 3.2.0</b>
* Database: <b>H2</b>

This project is intended for demonstration purposes and can be further extended to include additional functionalities.

<div id="features" />

## ✨ Features 

Here're some of the project's best features:

-   **Import & Management:**  Import quiz results from Microsoft Forms output xlsx files.
-   **Data Visualization:**  View results in a sortable table with:
    -   Nickname
    -   Number of correct answers
    -   Timestamp
    -   Awarded prize (or lack thereof)
-   **Reward System:**
    -   Define reward categories.
    -   Configure reward allocation strategies (e.g., percentage of top scorers, number of correct answers).
    -   Manually edit awarded prizes for corrections.
-   **Data Export:**  Export results with assigned awards to xlsx and pdf formats, including visual distinction for prize recipients.
-   **Quiz Statistics:**  Analyze quiz performance through:
    -   Percentage of correct answers for each question.
    -   Distribution of answers for a specific selected question.

<div id="setup" />

## 🛠️ Setup 

**Prerequisites:**

-   **JDK 17 or later**: Download and install from  [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/). Verify installation by running  `java -version`  in your terminal.
-   **Gradle**: Download and install Gradle distribution from [https://services.gradle.org/distributions/gradle-8.4-bin.zip](https://services.gradle.org/distributions/gradle-8.4-bin.zip). Verify installation by running  `gradle --version`  in your terminal.

**Installation Steps:**

1.  **Clone Repository**:

Open a terminal or command prompt and navigate to your desired installation directory and clone repository using Git:

	git clone https://github.com/piaccho/quiz-review-app.git

Then navigate to the cloned project directory:

	cd quiz-review-app

    
2.  **Build and run**:

   Build and run the server by navigating to the `/master/backend` subdirectory and run the following command:

    gradle bootRun
       
   To build and run the client, from the root directory of application, navigate to the `/master/frontend` subdirectory and run the following command:

	gradle run

<b>Note</b>: If you use an IDE that supports Gradle (e.g. IntelliJ IDEA), you can easily run both the server and the client.

<div id="example-of-use" />

## 💻 Example of use 

## Credits

This project was developed by the following team members:

- Sebastian Piaskowy - Main contributor and maintainer.
- Zofia Lenart - Main contributor and maintainer.
- Norbert Żmija - Minor features to the project.
