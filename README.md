# SAE302 - Nom de votre projet

## Instructions pour le projet

1. Clonez ce projet (il suffit de faire un clone par groupe projet). Pour cela, vous devez exécuter la commande suivante dans le répertoire où vous voulez mettre votre dépôt local : `git clone https://scm.univ-tours.fr/soulet/sae302-template.git sae302-projet`
Notez que le projet `sae302-template` est seulement accessible aux membres de l'université. Cette commande va créer un répertoire contenant une copie du dépôt distant.
2. Modifiez le fichier README.md pour indiquer le nom de votre projet.
3. Poussez votre dépôt local sur le GitLab de l'université en changeant son nom.
* Ajoutez le fichier à l'index : `git add README.md`
* Faites le commit pour valider les changements sur le dépôt local : `git commit -m "First release!"`
* Modifiez l'origine du dépôt distant pour mettre https://scm.univ-tours.fr/identifiant/sae302-projet.git
* Poussez votre projet sur le dépôt distant
```
git remote rm origin
git remote add origin https://scm.univ-tours.fr/soulet/sae302-projet.git
git push origin main
```
4. Invitez les autres membres de groupe pour qu'ils rejoignent votre projet. Pour le rôle, vous devez sélectionner mainteneur afin de pouvoir travailler directement sur la branche principale du projet.
5. Clonez ce projet pour les autres membres afin de pouvoir travailler sur le même dépôt.
6. Connectez-vous à votre base de données MariaDB en utilisant MySQL Workbench et en créant une nouvelle connexion :
* Hôte : 10.195.25.15
* Port : 3306
* Identifiant de connexion : no d'etudiant + lettre t (par exemple 22007009t)
* Mot de passe (ne pas retenir) : SGBD-Blois
7. Modifiez votre mot de passe qui sera partagé avec les autres membres du projet (car il faut une seule base de données par projet). Pour cela, utilisez la commande SQL suivante : `SET PASSWORD FOR ``identifiant``@``%`` = PASSWORD('nouveau');` 
8. Exécutez le script de création de la table dans le répertoire sql du projet et vérifiez avec des requêtes que les données sont bien crées.
9. Créez et configurez un projet sous Eclipse à partir du dossier existant.
* Créez un nouveau projet Java en sélectionnant le répertoire du dépôt local.
* Ajoutez la librairie `mariadb-java-client-3.0.8.jar` au `Build Path` afin de pouvoir utiliser ce driver pour la connexion. 
* Ajoutez un fichier fichier properties/configuration.properties avec les informations ci-dessous à compléter :
```
db_host=10.195.25.15:3306
db_user=
db_pwd=
db_name=
```
Notez qu'il est important de ne pas mettre sur le dépôt distant ce fichier de configuraution car il contient des informations sensibles (même si le dépôt est uniquement ouvert en interne).
10. Exécutez le programme Java de sorte à tester la connexion à la base de données.
11. Supprimez ces instructions et conservez la partie ci-dessous pour décrire votre projet (notamment pour les livrables 2 et 3). Il s'agit juste d'un exemple de README que vous pouvez personnaliser à votre guise.

## README

### A propos de ce projet

Décrivez brièvement l'objectif de ce projet.

Ce projet a été réalisé par :
* Etudiant 1
* Etudiant 2

### Principales caractéristiques

Les principales caractéristiques du projet sont les suivantes :
* Caractéristique 1
* ...
N'hésitez pas préciser si certaines caractéristiques ne fonctionnent pas encore correctement, celles ajoutées par rapport au livrable précédent, celles en cours d'implémentation, etc.

### Comment installer l'application ?

Décrivez comment installer votre application :
* Quel script exécuter pour créer les tables ?
* Comment configurer l'application côté serveur/côté client
* Que faut-il démarrer ?

### Comment utiliser l'application ?

Décrivez comment utiliser votre application.




