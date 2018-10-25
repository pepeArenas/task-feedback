# task-feedback
In order to run MySQL container we need to move to root of management-core and run this command
docker-compose up -d
This will create the schema, tables and stored procedures that will need
If we want to stop the mysql instance we type this in a terminal
docker-compose stop
If we want to delete al the containers that are stopped we can type the following in a terminal
docker system prune
An we type a y for yes

management-web will run:
	localhost:8080

management-core will run:
	localhost:8081

MySQL docker container will run:
	localhost:3306

Tips
If we move something from data.sql or from the docker configuration is better to delete the
container and recreate it

For delete
Stop all the docker containers with:
docker-compose stop  
One stopped run the following command in order to remove them
docker-compose rm
In order to recreate them run the following:
docker-compose up --build -d
