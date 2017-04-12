Lecteur PDF UPnP (pouvant être contrôlé par d'autres composants UPnP).

Installation:

Le repertoire build contient les éléments nécessaires pour éxecuter l'application: le fichier .jar et les dll
Il suffit de lancer le fichier .jar pour éxecuter l'application



Specification UPnP:

Le device Lecteur PDF offre le service VisionneuseService, dont voici la description:

   1) SetTarget(String newTargetValue): newTargetValue pouvant être les chaînes DROITE ou GAUCHE respectivement pour avancer et reculer dans le défilement des slides.
   2) GetNbPages(): retourne le nombre de pages total du document ouvert (dans une variable ResultNbPages)
   3) GetNumPage(): retourne le numéro de la page courante (dans une variable ResultNumPage)


Maintenance:
C'est un projet Maven
la librairie jmupdf n'a pas de depot Maven, il faut donc l'ajouter manuelement
Pour cela il suffit de copier le repertoire "jmupdf-core" dans le repository local Maven  
Effectuer les modifications souhaitées
Ajouter une configuration d'éxecution Maven avec la phase "package" pour exporter en .jar 
Executer cette commande; deux fichiers .jar sont crées dans un nouveau dossier target 
   Visionneuse-0.0.1-SNAPSHOT-jar-with-dependencies.jar qui est le bon éxecutable
   
   Visionneuse PDF utilise la librairie Java jmupdf(licence GPLv3) qui elle même est basée sur mupdf(licence AGPL) 
Le repertoire lib se trouvant à la racine du projet contient les dll de mupdf et le .jar de jmupdf(dans le repertoire jmupdf-core)
