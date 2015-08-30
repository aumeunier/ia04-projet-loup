# Introduction #
Cette section détaille la communication entre les agents: quelles sont les informations échangées, à quel moment et de quelle manière. Pour la liste des agents et de leurs rôles respectifs, se référencer à la section Agents du wiki.

# Conteur et Joueur #
Le Conteur est le contrôleur de la partie, les Joueurs sont les personnes qui jouent et qui peuvent "jouer" un rôle. Les Joueurs ont une "mémoire" de partie en partie.
  * Enregistrement d'un joueur auprès du conteur:
> Un joueur qui rentre dans une nouvelle partie doit s'enregistrer auprès du conteur.
    1. Le joueur se connecte et demande un enregistrement auprès du conteur
    1. Le conteur valide ou non l'inscription

  * Désinscription d'un joueur
> Lorsqu'un joueur quitte la partie il doit se désinscrire.
    1. Envoie d'un message du Joueur à Conteur
    1. Le Conteur désinscrire le Joueur et peut le remplacer par un bot (version ultérieure) ou le remplacer par un mort si jamais la partie est en cours

  * Lancement d'une partie
> En début de partie le conteur demande aux joueurs connectés s'ils veulent jouer.
    1. Envoie d'un message début de partie de Conteur à tous les joueurs qui se sont enregistrés auprès de lui
    1. Réponse du Joueur: rentre ou non dans la partie
    1. Le Conteur obtient le nombre total de joueurs souhaitant jouer, détermine les rôles à utiliser suivant ce nombre et réparti les rôles aléatoirement. Il émet la liste des rôles
    1. Chaque joueur reçoit son rôle et l'instancie, il le lie à soi-même ainsi qu'à sa GUI pour accélérer les communications entre GUI et programme.

  * Fin d'une partie
    1. Le Conteur indique à tous les joueurs que la partie est terminée
    1. Le Joueur supprime son rôle

# Joueur et Rôle #
Chaque Joueur peut jouer un rôle pendant une partie. Les Rôles représentent les différents personnages du jeu du Loup Garou de Thiercelieux.
  * Début d'une partie
    1. Le Joueur envoie des informations au Rôle (mémoire et stratégies)
    1. Le Rôle reçoit ces données et les traite
  * Fin d'une partie
    1. Le Joueur notifie le Rôle de la fin de partie
    1. Le Rôle envoie des informations sur la partie au Joueur
    1. Le Joueur stocke les informations et supprime le Rôle

# Communication et Rôle #
## Vote ##
  1. Vote déclenche un vote avec un type (loup, pendu, maire, nomination du maire)
  1. Les joueurs pouvant voter répondent
  1. Vote traite les votes et si nécessaire effectue de nouveaux tours de vote

## Conseil ##
  1. Conseil demande des conseils à chaque joueur
  1. Les joueurs envoient leurs conseils
  1. Conseil traite les conseils, les indique à tout le monde et effectue de nouveaux tours si nécessaire

## Action ##
  * Action spécifique
    1. Déclenche la demande d'une action d'un rôle spécifique
    1. Si le Joueur ayant le rôle existe et est vivant, il répond à cette action

  * Démarrage d'une partie
    1. Communication indique à tous les Rôles que la partie commence
    1. Les Rôles récupèrent les AID de tous les autres joueurs afin de savoir pour qui ils peuvent voter (pas de communication directe entre les deux)

  * Mort d'un joueur

## Rôle ##
Le Rôle s'enregistre auprès de chaque Agent Communication (Vote, Conseil, Action)

# Conteur et Communication #
  * Vote
    1. Conteur lance un vote (si Bouc-Emissaire, l'indique à l'AgCom)
    1. Communication renvoie le résultat du vote
  * Action (spécifique ou globale)
    1. Conteur lance l'action
    1. Communication renvoie le résultat de l'action (résultat ou vide si phase finie mais pas d'action)
  * Demande phase de conseil
  * Indique la mort d'un joueur


# Messages #
## Conteur - Joueur ##
{

> type: YOU\_DIE, ACCEPT\_PLAYER, START\_GAME, ATTRIBUTE\_ROLE, END\_GAME, PHASE, .. , REGISTER, START\_GAME, LEAVE\_GAME, STORYTELLING

> rôle:  ..

> phase: JOUR, NUIT,...

> value (START\_GAME): YES, NO

}

## Joueur - Rôle ##
Osef for now

## Communication - Rôle ##
### Vote ###
{
> type: KILL\_PAYSAN, KILL\_LG, ELECT\_MAYOR, SUCCESSOR

> nbVoix: int

> choix: LocalName

}

### Conseil ###
?

### Action ###
?