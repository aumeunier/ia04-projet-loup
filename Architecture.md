AgControleur:

- Affecter un rôle à un joueur

- Gérer l’alternance jour/nuit

- Stocker les informations générales de la partie

> - Période (nuit/jour)

> - Liste des joueurs et leur rôles

- Consulter les règles dans une base de connaissances (base gérée dans un agent séparée)

- Accepter un nouveau joueur

AgJoueur:

- Garder les informations sur les parties précédentes

- Stocker et récupérer les informations sur les stratégies (via les agents KB ?)

- En relation avec un AgGUI

- Initialiser son rôle suivant l’affectation que lui a faite l’AgControlleur

- S’enregister auprès du Controleur

AgRôle:

- Stocker les niveaux de confiances de la partie en cours

- Créer des behaviors suivant son rôle

- Héritage -> AgRoleX

- Contenir les stratégies (méthodes de classe)

AgKb:
> - Instancier une base de connaissance
> - Requêter une base de connissance

AgCommunication: (le nom est pas top)
> - Organiser un vote pour éliminer un loup garou
> - Organiser un vote pour éliminer un paysan
> - Organiser un vote pour élire le maire

AgGui: (discuter, plusieurs interfaces différentes, un seul agent ?)
> - Proposer un interface graphique à un humain