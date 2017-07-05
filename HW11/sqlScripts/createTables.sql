CREATE TABLE tuser (
  id         bigserial CONSTRAINT pUserkey PRIMARY KEY,
  name       varchar(255) NULL,
  age        integer NOT NULL DEFAULT 0,
  address_id bigint  NULL REFERENCES taddress(id)
);
GRANT ALL PRIVILEGES ON TABLE tuser TO userdb;
GRANT ALL PRIVILEGES ON SEQUENCE tuser_id_seq TO userdb;


CREATE TABLE taddress (
  id     bigserial CONSTRAINT pAddrkey PRIMARY KEY,
  street varchar(255) NULL,
  index  integer      NULL
);
GRANT ALL PRIVILEGES ON TABLE taddress TO userdb;
GRANT ALL PRIVILEGES ON SEQUENCE taddress_id_seq TO userdb;

CREATE TABLE tphone (
  id     bigserial CONSTRAINT tPhonekey PRIMARY KEY,
  userId bigint    REFERENCES tuser(id),
  code   integer      NULL,
  number varchar(255) NULL
);
GRANT ALL PRIVILEGES ON TABLE tphone TO userdb;
GRANT ALL PRIVILEGES ON SEQUENCE tphone_id_seq TO userdb;

