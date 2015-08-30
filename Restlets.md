# Introduction #

Deux services Restlet doivent être intégrés au projet IA04. Un de ces projets doit être externe, l'autre peut être local.


# Services Restlet #
Les services auxquels nous avons pensé sont:
> - Service d'observation de la partie à plusieurs niveaux
  * Observateur simple (même chose qu'un joueur mort)
  * Observateur omniscient (informations totales sur la partie)
  * Conteur (même chose qu'un joueur mort + connaissances de tous les rôles)

> - Interaction avec Twitter

### Obtenir nom des joueurs dans la partie ###

### Obtenir information sur un utilisateur ###
PARAM=name (ou id)
Return: erreur
ok 200 - nom de l'utilisateur, id, état (vivant/mort), rôle actuel, rôles secondaires, stratégies, autres (nombre de potions restantes

### Obtenir l'état actuel de la partie ###
But = Obtenir le rôle de chacun des joueurs et leur état

### Obtenir les retours console (messages importants envoyés aux agents) ###
But = debug et/ou observer la partie de manière plus détaillée

### Obtenir toutes les informations ###
Implique toutes les informations ci-dessus ainsi que les niveaux de confiance pour les bots