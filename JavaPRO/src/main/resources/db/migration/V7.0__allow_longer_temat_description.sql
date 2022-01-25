ALTER TABLE projekty.tematy
ALTER
COLUMN description TYPE VARCHAR(1000) USING (description::VARCHAR(1000));