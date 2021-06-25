/** Create DML and DDL ROLES */
DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT *
                FROM pg_catalog.pg_group
                WHERE groname = 'cryptobot_dml'
            )
        THEN
            EXECUTE 'CREATE ROLE cryptobot_dml;';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT *
                FROM pg_catalog.pg_group
                WHERE groname = 'cryptobot_ddl'
            )
        THEN
            EXECUTE 'CREATE ROLE cryptobot_ddl;';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT *
                FROM pg_catalog.pg_group
                WHERE groname = 'cryptobot_ro'
            )
        THEN
            EXECUTE 'CREATE ROLE cryptobot_ro;';
        END IF;
    END
$$;

/** Create application user and flyway application user with default passwords */
DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT *
                FROM pg_catalog.pg_roles
                WHERE rolname = 'cryptobot'
            )
        THEN
            EXECUTE 'CREATE ROLE cryptobot LOGIN PASSWORD ''cryptobot'' VALID UNTIL ''infinity'';';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT *
                FROM pg_catalog.pg_roles
                WHERE rolname = 'cryptobot_dba'
            )
        THEN
            EXECUTE 'CREATE ROLE cryptobot_dba LOGIN PASSWORD ''cryptobot_dba'' VALID UNTIL ''infinity'';';
        END IF;
    END
$$;

/* Setting privileges for dml and ddl, while revoking all from public. */
DO
$$
    BEGIN
        EXECUTE 'GRANT CONNECT ON DATABASE ' || current_database() ||
                ' TO cryptobot_ddl, cryptobot_dml, cryptobot_ro;';
        EXECUTE 'GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public' ||
                ' TO cryptobot_dml;';
        EXECUTE 'GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO cryptobot_ddl;';
        EXECUTE 'GRANT ALL ON DATABASE ' || current_database() || ' TO cryptobot_ddl;';
        EXECUTE 'REVOKE ALL ON DATABASE ' || current_database() || ' FROM public;';
    END
$$;

/* Grant our application user and flyway user the correct role and privileges to get started */
GRANT USAGE ON SCHEMA PUBLIC TO cryptobot_dml;
GRANT USAGE ON SCHEMA PUBLIC TO cryptobot_ro;
GRANT cryptobot_dml TO cryptobot;
GRANT cryptobot_ddl TO cryptobot_dba;
REVOKE ALL ON SCHEMA PUBLIC FROM PUBLIC;
GRANT ALL ON SCHEMA PUBLIC TO cryptobot_ddl;