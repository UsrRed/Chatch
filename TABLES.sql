CREATE TABLE utilisateur(
	id_utilisateur INT auto_increment not null,
	nom_utilisateur VARCHAR(32) unique not null,
    	adresse_email VARCHAR(50) unique not null,
    	motdepasse VARCHAR(150) not null, #Ne pas oubler que le sha va augmenter drastiquement la qtt de caractères
    	description_utilisateur VARCHAR(500),
    	photo_utilisateur VARCHAR(32),
  	PRIMARY KEY (id_utilisateur)
);

CREATE TABLE connexions(
	id_connection INT auto_increment not null,
    id_utilisateur INT,
    	adresse_ip VARCHAR(500), # Ne pas mettre unique pour relever la dernière co et aussi car un user peut se co avec plusieurs comptes sur une seule IP
    	date_co date,
    	PRIMARY KEY (id_connection)
    	FOREIGN KEY (id_utilisateur) 
		REFERENCES utilisateur(id_utilisateur) 
        	ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE discussion(
	id_discussion INT auto_increment not null,
    	nom_discussion VARCHAR(32),
    	photo_discussion VARCHAR(32),
   	PRIMARY KEY(id_discussion)
);

CREATE TABLE message(
	id_message INT auto_increment not null unique,
    	id_discussion INT, # Ne pas mettre not null et lors de la suppression du message ne supprimer uniquement cet INT pour garder une trace des messages
	id_utilisateur INT,
    	contenu VARCHAR(1000) not null,
    	date_message TIMESTAMP not null,
    	type_message INT, #idéalement not null
    	PRIMARY KEY(id_message),
	FOREIGN KEY (id_discussion) 
		REFERENCES discussion(id_discussion),
    	FOREIGN KEY (id_utilisateur) 
		REFERENCES utilisateur(id_utilisateur) 
);

CREATE TABLE groupe_discussion(
	id_utilisateur INT not null,
	id_discussion INT not null,
	role INT,
	FOREIGN KEY (id_utilisateur) 
		REFERENCES utilisateur(id_utilisateur),
	FOREIGN KEY (id_discussion) 
		REFERENCES discussion(id_discussion)
);

