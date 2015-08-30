# Introduction #
Cette section décrit les rôles que nous souhaitons implémenter dans le jeu. Ces rôles sont assez similaires à ce du jeu original mais les descriptions seront plus orientées vers l'implémentation. Seront indiqués les Roles et les Behaviour utilisés pour modéliser le personnage. Pourront aussi être décrites les stratégies de chaque rôle. Ces stratégies permettront d'aider les bots à déterminer quand et comment appliquer leurs rôles spéciaux.

# Spécial #
## Observateur ##
## Mort ##
## RoleBase ##
Tous les autres rôles du jeu héritent de ce **RoleCore** qui donnera quelques caractéristiques et méthodes de base. Un **RoleCoreBehaviour** permettra de répondre à certains messages généraux comme la mort du personnage, la fin d'une partie, le début d'une partie ou la révélation d'un personnage.

# Jeu de base #
## Villageois ##
Personnage de base. Peut se présenter en tant que maire, élire le maire en début de partie et voter pour la personne à pendre. Le but d'un villageois sera de survivre à la partie et donc d'éliminer les loups-garous.

En termes d'implémentation le **RoleVillageois** est le rôle par défaut si le joueur n'obtient pas un rôle spécial. Il existera également un **BehaviourVillageois** que la quasi-totalité des rôles auront.

## Loup-Garou ##
Rôle fréquent que bon nombre de joueurs auront. Le loup-garou s'éveille la nuit et peut s'entendre avec les autres loups pour désigner une cible à tuer. Le but du loup-garou est de survivre à la partie et donc d'éliminer tous les villageois.

En termes d'implémentation le **RoleLoupGarou** est un rôle simple. Les loups-garous auront un BehaviourVillageois et un **BehaviourLoupGarou**. Ce dernier permettra d'agir la nuit pour la chasse des loups-garous.

## Maire ##
Rôle détenu par une personne à tout moment de la partie. Le maire peut-être n'importe quel villageois. Il est souvent suspect. Son rôle est normalement d'aider le village à survivre en le guidant par ses bonnes décisions. C'est lui qui prendra des décisions dans le cas d'ex-aequo. S'il est pendu par le village il doit nommer son successeur.

En termes d'implémentation le maire est modélisé par un simple **BehaviourMaire** qui lui permet de répondre aux messages destinés uniquement au maire: rompre un ex-aequo, nommer son successeur. Il n'est pas nécessaire d'utiliser un RoleMaire étant donné que le maire est forcément relié à un autre rôle, que ce soit un paysan, un loup-garou ou un rôle spécial.

## Sorcière ##
Ce rôle ne peut être détenu que par une seule personne au maximum. La sorcière possède deux potions, l'une pouvant sauver quelqu'un, l'autre pouvant tuer quelqu'un. La nuit, après le vote des loup-garous elle peut décider de sauver la cible si elle a encore sa potion de vie, et décider de tuer une autre si elle a encore sa potion de mort.

En termes d'implémentation la sorcière est modélisée par un **RoleSorcière** qui possède deux potions. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourSorciere** qui répondra aux messages destinés uniquement à la sorcière ("Veux-tu utliser tes potions ?").

## Voyante ##
Ce rôle ne peut être détenu que par une seule personne au maximum. La voyante peut observer, une fois par nuit, le rôle d'un joueur de son choix. Le rôle de voyante permet donc de savoir qui est qui et d'agir en conséquence. En revanche c'est un rôle très dangereux pour les loups-garous, la voyante doit donc agir discrètement pour ne pas être la prochaine cible.

En termes d'implémentation la voyante est modélisée par un **RoleVoyante**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourVoyante** qui répondra aux messages destinés uniquement à la voyante ("Quelle personne veux-tu observer ?").

## Chasseur ##
Ce rôle ne peut être détenu que par une seule personne au maximum.  Le chasseur peut, lorsqu'il est tué par les loups-garous, tuer une personne de son choix.

En termes d'implémentation le chasseur est modélisé par un **RoleChasseur** qui possèdera deux behaviours: le BehaviourVillageois et le **BehaviourChasseur** qui répondra aux messages destinés uniquement au chasseur ("Qui veux-tu tuer avant de mourir ?").

## Cupidon ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Cupidon n'a un rôle qu'en début de partie: il peut désigner deux personnes qui seront désormais des Amoureux (voir ci-dessous).

En termes d'implémentation Cupidon sera modélisé par un **RoleCupidon** qui indiquera qui choisir (stratégie) et se souviendra des deux personnes liées. Un **BehaviourCupidon** lui permettra de répondre au message destiné à Cupidon en début de partie ("Qui veux-tu désigner comme étant Amoureux?").

## Amoureux ##
Ce rôle est détenu par deux personnes exactement. Les Amoureux ont eu leur sort lié par Cupidon en début de partie. Ils doivent faire en sorte qu'ils soient les deux derniers survivants, peu importe qu'ils soient loups-garous et/ou paysans. Les Amoureux connaissent par ailleurs le rôle de leur amoureux.

En termes d'implémentation les amoureux sont modélisés à l'intérieur du RoleCoreBehaviour (AID de son amoureux, null si pas d'amoureux). Certaines stratégies peuvent être modifiées si la personne est amoureuse, elle aura ainsi une confiance absolue en son amoureux et fera en sorte que cette personne survive à tout prix.

## Voleur ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Le Voleur peut, en début de partie, choisir son rôle entre deux cartes restantes. Si une des deux cartes indique le rôle de Loup-Garou le voleur doit la prendre.

En termes d'implémentation le Voleur sera modélisé d'une manière un peu spéciale: le Conteur indiquera au joueur que c'est lui le Voleur et qu'il a le choix entre deux cartes. Le joueur pourra choisir entre ces deux rôles et agira suivant ce rôle.

## Petite fille ##
Ce rôle ne peut être détenu que par une seule personne au maximum. La petite fille peut observer les agissements des loups-garous pendant la nuit. Elle peut ainsi essayer de détecter qui est un loup-garou. En revanche, si la petite fille est repérée par les loups elle est automatiquement désignée comme cible des loups pour cette nuit.

En termes d'implémentation la petite fille est modélisée par un **RolePetiteFille**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourPetiteFille** qui répondra aux messages destinés uniquement à la petite fille. Le RolePetiteFille indiquera quel risque elle souhaite prendre pour la nuit (plus le risque pris est grand, plus elle a de chances de repérer les loups-garous mais plus elle a de chances de se faire repérer).

# Première extension: Nouvelle Lune #
## Bouc-Emissaire ##
Ce rôle ne peut être détenu que par une seule personne au maximum. En cas d'égalité de votes pour la personne à pendre, c'est le Bouc-Emissaire qui sera tué. Il peut alors désigner les personnes qui pourront voter au tour suivant. Si toutes ces personnes sont tuées d'ici au tour suivant, personne n'est tué au tour suivant.

En termes d'implémentation le Bouc-Emissaire est modélisée par un **RoleBoucEmissaire**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourBoucEmissaire** qui répondra aux messages destinés uniquement au Bouc-Emissaire.

## Joueur de flûte ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Le joueur de flûte est ennemi des villageois et des loups-garous. Son but est de charmer tous les joueurs de la partie et de rester en vie. Il doit, à la fin de chaque nuit, se réveiller et charmer deux nouvelles personnes. Ces personnes sont au courant qu'elles sont charmées et des autres personnes charmées.

En termes d'implémentation le Joueur de flûte est modélisée par un **RoleJoueurFlute**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourJoueurFlute** qui répondra aux messages destinés uniquement au Joueur de flûte.

## Salvateur ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Le salvateur peut, juste avant le réveil des loups-garous, désigner une personne qui sera sauvée ce tour-ci si elle est ciblée par les loups-garous. Le salvateur ne peut pas cibler deux fois de suite la même personne.

En termes d'implémentation le Salvateur est modélisée par un **RoleSalvateur**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourSalvateur** qui répondra aux messages destinés uniquement au Salvateur.

## L'idiot du village ##
Ce rôle ne peut être détenu que par une seule personne au maximum. L'idiot du village ne peut être pendu par le village. Après son rôle révélé, il ne peut plus voter ou être maire (et s'il était maire, plus personne ne sera maire jusqu'à la fin de la partie).

En termes d'implémentation l''idiot du village est modélisée par un **RoleIdiot**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourIdiot** qui répondra aux messages destinés uniquement à l'Idiot du village.

## L'ancien du village ##
Ce rôle ne peut être détenu que par une seule personne au maximum. L'ancien du village survit à la première attaque des loups-garous contre lui. S'il est pendu par le village, tous les personnages perdent leurs pouvoirs à l'exception des loups-garous et du joueur de flute.

En termes d'implémentation l''idiot du village est modélisée par un **RoleAncien**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourAncien** qui répondra aux messages destinés uniquement à l'Ancien du village.

# Deuxième extension: Le Village #
## Corbeau ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Le corbeau peut, chaque matinée, désigner une personne qu'il pense être loup-garou. Cette personne aura automatiquement deux voies contre elle dans la journée, voies que tout le monde sait au matin.

En termes d'implémentation le corbeau est modélisé par un **RoleCorbeau**. Ce rôle possèdera deux behaviours: le BehaviourVillageois et le **BehaviourCorbeau** qui répondra aux messages destinés uniquement au Corbeau. Le RoleCorbeau indiquera la personne qu'il trouve la plus suspecte à son tour spécial.

## Loup-Garou blanc ##
Ce rôle ne peut être détenu que par une seule personne au maximum. Le loup-garou blanc est un loup comme les autres mais qui possède un pouvoir supplémentaire: celui de tuer un autre loup-garou, une nuit sur deux. Son but est d'être le dernier survivant.

En termes d'implémentation le corbeau est modélisé par un **RoleLoupGarouBlanc**. Ce rôle possédera trois behaviours: le BehaviourVillageois, le BehaviourLoupGarou et le BehaviourLoupGarouBlanc. Le BehaviourLoupGarouBlanc lui permettra de répondre aux messages destinés uniquement au Loup-Garou Blanc.