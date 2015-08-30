# Introduction #
Cette section contient une définition des termes propres au projet.

### Joueur Bot ###
Un joueur bot est un "joueur" qui n'est pas un joueur humain. Ce joueur possède un rôle comme tout autre joueur mais est contrôlé par intelligence artificielle. Les bots ne disposent pas de plus d'informations sur la partie actuelle que les autres joueurs mais disposent d'une base de connaissance interne au programme leur permettant d'élaborer des stratégies. Leurs choix sont essentiellement orientés suivant leur(s) stratégie(s) actuelle(s) et leurs niveaux de confiance.

### Niveaux de confiance ###
Les niveaux de confiance permettent aux joueurs bots de faire leur choix au cours de certaines phases de jeu: élection d'un nouveau maire, choix du paysan à tuer, choix du joueur à pendre. Chaque joueur dispose de ses propres niveaux de confiance qui sont cachés aux autres (simulation des pensées d'un joueur humain). Le joueur dispose par ailleurs d'un niveau de confiance associé à chaque autre joueur dans la partie. Ces niveaux de confiance sont actualisés à chaque action d'importance et pendant laquelle le bot perçoit des informations (un joueur paysan endormi n'obtiendra pas d'informations pendant la nuit à moins de repérer un loup-garou).

Le "niveau de confiance" est relié de manière directe au "degré de suspicion" et au "degré de dangerosité" qui ne sont que d'autres noms pour cet indicateur (le degré de suspicion et le degré de dangerosité sont inverses au niveau de confiance).

### Repérer un loup ###
Pendant la phase de nuit, les loups agissent mais peuvent mettre du temps à s'accorder sur la cible à tuer. Dans le "vrai" jeu, il est souvent possible de deviner qui est loup-garou aux mouvements d'air et aux sons qui peuvent être générés par les signes répétés des joueurs proches. Ceci est simulé dans notre jeu par une possibilité de "repérer" les loups. En effet, à chaque itération dans le choix de la cible à tuer, les loups ont un pourcentage de chances de se faire repérer. Dès lors, le joueur ayant repérer le loup-garou saura que cette personne est un loup, ou du moins, aura une très forte suspicion envers cette personne.