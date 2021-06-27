#!/bin/bash
set -e

#setup database exactly as done locally
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -a -f /tmp/0_database.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "cryptobot" -a -f /tmp/1_roles_users.sql