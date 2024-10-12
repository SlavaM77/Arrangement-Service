CREATE OR REPLACE FUNCTION update_modified_row()
    RETURNS TRIGGER AS
'
    BEGIN
        NEW.updated_at = NOW();
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
    BEFORE UPDATE
    ON groups
    FOR EACH ROW
EXECUTE FUNCTION update_modified_row();
