CREATE USER school_user WITH PASSWORD '1234';
CREATE DATABASE school_db WITH OWNER = school_user;

GRANT ALL PRIVILEGES ON DATABASE school_db TO school_user;
