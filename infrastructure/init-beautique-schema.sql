DO $$
BEGIN
    IF NOT EXISTS(
        SELECT schema_name
        FROM information_schema.schemata
        WHERE schema_name = 'beautique'
    ) THEN
        EXECUTE 'CREATE SCHEMA beautique_schema';
    END IF;
END
$$;