FROM mysql:8.0.12

MAINTAINER Jesus Arenas <jesusarenasm@hotmail.com>

# Copy the database initialize script: 
# Contents of /docker-entrypoint-initdb.d are run on mysqld startup
ADD  ./mysql/ /docker-entrypoint-initdb.d/