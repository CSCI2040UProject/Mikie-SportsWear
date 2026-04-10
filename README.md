# Mikie Sportswear

## Overview
Mikie Sportswear is an online sportswear catalog that allows users to view products and their pricing. Users can search products, sort them by price and filter by colour, product ID, gender, price, and many more categories. 

There are two access levels: user and admin. The data is pulled from a CSV file and accessed with an SQL database, which can be changed by admins only; they have permissions to add, delete, and edit products. Admins also have the permission to grant admin access to user accounts.

## Development Guide
### Main Languages/Tools used
**Backend:** Java

**Build Tool:** Maven

**Runtime Environment and Package Manager:** Node.js and npm

**Frontend:** JSX + CSS

### Backend Breakdown

The backend is composed of the SQL database (in the `data` folder), the CSV files of the product data/user data, the test functions and the numerous components that make up the architectural pattern. The latter consists of the well-known MVC pattern (model-view-controller) with some extra components; the  `repository`, where the raw data is handled, and the `database`, where the raw data is converted into SQL and retrieved from it.

### Frontend Breakdown

This is the "View" part of our model, and it's what the user interacts with. It's written using JSX and CSS consists of all the components of the website (e.g. `FrontPage.jsx`, `Profile.jsx`, `Item.jsx`) and their styles (located in `styles` folder as CSS files).

## Build Instructions

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
- ‘npm start’ to run the program, access website with localhost:5173
- Control + C in the terminal to terminate the local server
