# --- !Ups

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
  user_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
  user_code VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

INSERT INTO users VALUES (uuid_generate_v4(), 'stest',
  '$2a$10$mQq0TybuB7fL2XQS4leQ3ONKuXWmsO0/9nX/wx.oHJcF1uoHsHZJ.');

# --- !Downs

DROP TABLE users;
DROP EXTENSION "uuid-ossp";
