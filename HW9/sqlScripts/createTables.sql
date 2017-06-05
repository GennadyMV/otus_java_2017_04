CREATE TABLE tuser (
  id    bigserial CONSTRAINT pkey PRIMARY KEY,
  name  varchar(255) NULL,
  age   smallint NOT NULL DEFAULT 0
);