-- Databasenavn: waplj_prosjekt
-- Brukernavn = passord = waplj

DROP TABLE trening;
DROP TABLE kategori;
DROP TABLE rolle;
DROP TABLE bruker;

CREATE TABLE rolle(
brukernavn varchar(10) NOT NULL,
rolle varchar(10) NOT NULL);
alter table rolle add primary key(brukernavn,rolle);

CREATE TABLE trening(
  oktnr        INTEGER GENERATED ALWAYS AS IDENTITY,
  dato         DATE NOT NULL,
  varighet     INTEGER NOT NULL,
  kategorinavn VARCHAR(15)NOT NULL,
  tekst        VARCHAR(30),
  brukernavn   VARCHAR(10)NOT NULL,
  CONSTRAINT trenings_pk PRIMARY KEY(oktnr));

CREATE TABLE bruker(
  brukernavn VARCHAR(10) PRIMARY KEY,
  passord    VARCHAR(10) NOT NULL);

CREATE TABLE kategori(
  kategorinavn VARCHAR(15)PRIMARY KEY);
  
ALTER TABLE rolle
    ADD CONSTRAINT rolle_fk1 FOREIGN KEY (brukernavn)
    REFERENCES bruker;

ALTER TABLE trening
  ADD CONSTRAINT trening_fk1 FOREIGN KEY (kategorinavn)
  REFERENCES kategori;
  
ALTER TABLE trening
  ADD CONSTRAINT trening_fk2 FOREIGN KEY (brukernavn)
  REFERENCES bruker;
  
INSERT INTO kategori(kategorinavn) VALUES('sykling');
INSERT INTO kategori(kategorinavn) VALUES('styrke');
INSERT INTO kategori(kategorinavn) VALUES('aerobics');
INSERT INTO kategori(kategorinavn) VALUES('jogging');

INSERT INTO bruker(brukernavn, passord) VALUES('anne', '1234');
INSERT INTO bruker(brukernavn, passord) VALUES('tore', '5678');

INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn)
            VALUES(DATE('2009-09-18'), 50, 'styrke', 'helsestudio', 'anne');
INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn)
            VALUES(DATE('2009-10-18'), 100, 'sykling', 'rundt Storvatnet', 'tore');
INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn)
            VALUES(DATE('2009-09-25'), 60, 'styrke', 'hjemme', 'anne');
INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn)
            VALUES(DATE('2009-09-18'), 45, 'aerobics', 'helsestudio', 'anne');

INSERT INTO ROLLE(brukernavn, rolle) VALUES('anne', 'bruker');
INSERT INTO ROLLE(brukernavn, rolle) VALUES('tore', 'admin');