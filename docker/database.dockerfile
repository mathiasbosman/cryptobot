FROM postgres

COPY ./db/setup/0_database.sql /tmp/
COPY ./db/setup/1_roles_users.sql /tmp/
COPY ./db/setup/database-setup.sh /docker-entrypoint-initdb.d/