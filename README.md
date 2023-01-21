# SAE302 - Nom de votre projet

## Instructions pour mettre en place votre environnement de développement

L'objectif est de mettre en place un environnement de développement collaboratif pour votre projet. Vous configurerez un projet Java sous Eclipse IDE avec un code source existant qui interrogera une base de données MariaDB. Chaque membre du groupe pourra développer de son côté et apporter les modifications sur un dépôt distant.

Les prérequis pour se lancer sont :
* Connaitre la constitution de son groupe
* Choisir un nom de projet sans espace pour construire le dépôt

Vous allez utiliser SCM qui est la plateforme collaborative de développement de l'Université de Tours dont l'adresse Web est [https://scm.univ-tours.fr/](https://scm.univ-tours.fr/). Cette plateforme est comparable à d'autres disponibles en ligne comme [GitHub](https://github.com/), mais SCM est directement **accessible à partir de votre compte étudiant** et elle est **gratuite**.

Voici plusieurs documentations utiles pour la suite :
* [Documentation GitLab](https://docs.gitlab.com/)
* [Principales commandes avec explications rapides](https://www.alternative-rvb.com/blog/git-aide-memoire/)
* [Aide-mémoire](https://training.github.com/downloads/fr/github-git-cheat-sheet.pdf)

1. Clonez ce projet (il suffit de faire un clone par groupe projet). Pour cela, vous devez exécuter la commande suivante dans le répertoire où vous voulez mettre votre dépôt local : `git clone https://scm.univ-tours.fr/soulet/sae302-template.git sae302-projet`
Notez que le projet `sae302-template` est seulement accessible aux membres de l'université. Cette commande va créer un répertoire contenant une copie du dépôt distant.
2. Avec un éditeur de texte, modifiez le fichier README.md pour indiquer le nom de votre projet. Notez que l'extension .md réfère au langage markdown. Vous pouvez vous appuyer sur cette [documentation du markdown](https://docs.gitlab.com/ee/user/markdown.html).
3. Poussez votre dépôt local sur le GitLab de l'université en changeant son nom.
* Ajoutez le fichier à l'index : `git add README.md`
* Faites le commit pour valider les changements sur le dépôt local : `git commit -m "First release!"`
* Modifiez l'origine du dépôt distant pour mettre https://scm.univ-tours.fr/identifiant/sae302-projet.git en utilisant des commandes du type  `git remote`. Il faut retirer l'ancienne origine et définir la nouvelle.
* Poussez votre projet sur le dépôt distant en utilisant la commande `git push`
4. Invitez les autres membres du groupe pour qu'ils rejoignent votre projet. Pour le rôle, vous devez sélectionner *mainteneur* afin de pouvoir travailler directement sur la branche principale du projet. Invitez *Arnaud Soulet* en tant qu'invité afin que je puisse voir votre projet pour le corriger.
5. Pour les autres membres du projet, il faut vous constituer un dépôt local surlequel travailler. Pour cela, clonez ce projet afin de pouvoir travailler sur le même dépôt et modifiez la liste des étudiants dans la partie README ci-dessous.
> Il est important à chaque modification de faire un `add`, un `commit` et un `push` pour pousser ses modifications sur le dépôt distant. Pour que les autres mettent à jour leur projet, ils doivent exécuter une commande `pull`. Pour éviter les conflits, il faut éviter de travailler sur les mêmes fichiers en même temps. Sinon, cela produit des conflits souvent difficiles à résoudre...
6. Connectez-vous à votre base de données MariaDB en utilisant MySQL Workbench et en créant une nouvelle connexion :
* Hôte : 10.195.25.15
* Port : 3306
* Identifiant de connexion : no d'etudiant + lettre t (par exemple 22007009t)
* Mot de passe (ne pas retenir) : SGBD-Blois
7. Modifiez votre mot de passe qui sera partagé avec les autres membres du projet (car il faut une seule base de données par projet). Pour cela, utilisez la commande SQL suivante : `SET PASSWORD FOR 'identifiant'@'%' = PASSWORD('nouveau');` 
8. Exécutez le script de création de la table dans le répertoire sql du projet et vérifiez avec des requêtes que les données sont bien créées.
9. Créez et configurez un projet sous Eclipse à partir du dossier existant.
* Créez un nouveau projet Java en sélectionnant le répertoire du dépôt local.
* Ajoutez la librairie `mariadb-java-client-3.0.8.jar` au `Build Path` afin de pouvoir utiliser ce driver pour la connexion. 
* Ajoutez un fichier `properties/configuration.properties` avec les informations ci-dessous à compléter :
```
db_host=10.195.25.15:3306
db_user=
db_pwd=
db_name=
```
> Il est important de ne pas mettre sur le dépôt distant ce fichier de configuraution car il contient des informations sensibles (même si le dépôt est uniquement ouvert en interne).
10. Exécutez le programme Java de sorte à tester la connexion à la base de données.
11. Supprimez ces instructions et conservez la partie ci-dessous pour décrire votre projet (notamment pour les livrables 2 et 3). Il s'agit juste d'un exemple de README que vous pouvez personnaliser à votre guise.

## README

### A propos de ce projet

Décrivez brièvement l'objectif de ce projet.

Ce projet a été réalisé par :
* Yohann DENOYELLE
* Eliott LEBOSSÉ

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




