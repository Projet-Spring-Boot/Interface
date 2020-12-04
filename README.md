# Interface graphique et optimisation de l'expérience utilisateur

**Ce repository est déstiné à heberger les sources du service dédié à l'interface utilisateur d'un projet visant à agréger tous les réseaux sociaux en un seul.**

## Installation
**Veuillez vous assurez que vous avez bien le fichier *application.properties* dans *src/main/ressources***

```
spring.thymeleaf.cache=false
 
# ===============================
# DATABASE
# ===============================
 
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/social?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tangui

 
# ===============================
# JPA / HIBERNATE
# ===============================
 
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

# ===============================
# I18N
# ===============================

spring.messages.basename=lang/messages
```

## Fonctionnalitées couvertes par les interfaces
- ### Visualiser les flux de ses différents réseaux sociaux sur la même page
L'utilisateur peut consulter les flux d'actualité de tout ses réseaux sociaux sur une page sans différence de mise en page. Il doit pouvoir tout de même savoir distinguer de quel reseau social vient la publication. 

- ### Publier sur plusieurs réseaux sociaux en même temps
L'utilisateur doit pouvoir réaliser une publication dans un ou plusieurs réseaux sociaux en même temps en respectant leur contraintes. 

-	### Associer son compte provenant d'un réseau social à son compte 

- ### Voir les informations de son compte utilisateur

- ### Se connecter grâce aux réseaux sociaux ou non

- ### S'inscrire via les réseaux sociaux ou non

## Fonctionnalitées d'ergonomie
- ### Drag&Drop de média lors d'une publication
- ### Utilisation sur un terminal mobile style smartphone

## Lexique
**Compte (Account) :** Association d'un nom, d'une adresse mail, d'un prénom, d'un nom, un mot de passe et d'un groupe de réseaux sociaux.

**Réseau social (Social Network) :** Internet a consacré la montée en puissance des réseaux sociaux, devenus pour certains de véritables médias sociaux, qui permettent aux internautes et aux professionnels de créer une page profil et de partager des informations, photos et vidéos avec leur réseau. Des espaces de partage qui se distinguent par leur utilité (personnel, professionnel, rencontres...), leur logo et leurs audiences. Les principaux réseaux sociaux sont : Facebook, Twitter, LinkedIn, Instagram, Snapchat, Copains d'avant, Viadeo ou encore MySpace. ([Journal Du Net](https://www.journaldunet.com/ebusiness/le-net/reseaux-sociaux/)).

**Réseau social associé (linked social network) :** Réseau social dont l'utilisateur a créé un lien entre son profile et celui du réseau social.

**Profile (Profil) :** Voir Compte.

**Flux (Feed) :** Ensemble de publications publiées par les utilisateurs dont l'utilisateur courant est abonné.

**Flux d'actualité (Newsfeed) :** Voir Flux.

**Publication (Post) :** Message texte accompagné ou non d'un média sur un réseau social.

**Médias (Media) :** Vidéo ou images accompagnant une publication.
