# Mikie Sportswear

### Overview
Mikie Sportswear is an online sportswear catalog that allows users to view products and their pricing. Users can sort, search and filter by colour, product ID, gender, price, and many more categories. 

There are two access levels: user and admin. The data is pulled from an SQL database, which can be changed by admins only; they have permissions to add, delete, and edit products. Admins also have the permission to grant admin access to user accounts.

### Run with docker:

- Install [Docker](https://docs.docker.com/get-started/introduction/get-docker-desktop/)
- Run [docker-compose.yml](./docker-compose.yml) with Intellij
- Go to [localhost:5173](http://localhost:5173/)

### Run in Terminal:

- Starting in the root folder, perform one-time installation of npm (Node Package Manager) using  the following commands:
`npm install`
`cd frontend`
`npm install`
- ‘npm start’ to build the program, access website with localhost:5173
- Control + C in the terminal to terminate the local server

