# Mikie Sportswear
A full-stack sportswear catalog application built using Java, SQL, and JSX.

## Overview
Mikie Sportswear is an online sportswear catalog that allows users to view products and their pricing. Users can search products, sort them by price and filter by colour, product ID, gender, price, and many more categories. 

There are two access levels: user and admin. The data is pulled from a CSV file and accessed through an SQL database, which can only be modified by admins. They have permission to add, delete, and edit products. Admins also have the permission to grant admin access to user accounts.

## Project Structure
```
Mikie-SportsWear/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   ├── resources/
│   │   └── test/
│   └── data/
├── documentation/
├── frontend/
│   ├── functions/
|   ├── public/
│   └── src/
│   │   ├── components/
│   │   └── styles/
├── node_modules/
├── .gitignore
├── docker-compose.yml
├── cloudbuild.yaml
├── package.json
├── package-lock.json
└── README.md
```
The classes of the backend are contained in packages in `main/`, and their contents are visualized with class diagrams in `documentation`. Example interactions with the frontend are visualized using state diagrams.
<img width="971" height="788" alt="UMLController" src="https://github.com/user-attachments/assets/e1d3ebe8-f1a9-47cc-8a9e-22d0756ac555" />

## Development Guide
### Main Languages/Tools used
**Backend:** Java

**Build Tool:** Maven

**Database Management:** SQLite

**Runtime Environment and Package Manager:** Node.js and npm

**Frontend:** JSX + CSS

**Testing:** JUnit 5

### Backend Breakdown

The backend follows an MVC (Model-View-Controller) architecture with additional components:

- **Model** — Represents core data structures (e.g., User, Item)
- **View** — Frontend UI components
- **Controller** — Handles business logic and requests
- **Repository** — Manages data access and persistence
- **Database Manager** — Handles SQL queries and database interactions

### Frontend Breakdown

This is the "View" part of our model, and it's what the user interacts with. It's written using JSX and CSS consists of all the components of the website (e.g. `FrontPage.jsx`, `Profile.jsx`, `Item.jsx`) and their styles (located in `styles` folder as CSS files).

## Run Instructions

### Run with docker:

- Install [Docker](https://docs.docker.com/get-started/introduction/get-docker-desktop/)
- Run [docker-compose.yml](./docker-compose.yml) with Intellij
- Go to [localhost:5173](http://localhost:5173/)

### Run in Terminal:
- Install [Maven](https://maven.apache.org/download.cgi)
- Starting in the root folder, perform one-time installation of npm (Node Package Manager) using  the following commands:
`npm install`
`cd frontend`
`npm install`
- `npm start`, access website with localhost:5173
- Control + C in the terminal to terminate the local server

### Standalone Website (for Users)
The website can be accessed from a user's perspective using this link: [https://mikie.rhysdeswart.ca/](url) 
