FROM debian:stretch-slim

MAINTAINER Jesus Arenas <jesusarenasm@hotmail.com>

# Copy the database initialize script: 
# Contents of /docker-entrypoint-initdb.d are run on mysqld startup
ADD  ./mysql/ /docker-entrypoint-initdb.d/