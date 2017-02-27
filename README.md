Visionneuse PDF utilise la librairie Java jmupdf(licence GPLv3) qui elle même est basée sur mupdf(licence AGPL) 
Le repertoire lib se trouvant à la racine du projet contient les dll de mupdf et le .jar de jmupdf
Compilation:
C'est un projet Maven, pour exporter l'application en .jar suivez les étapes suivantes
1- créer le repertoire "jmupdf-core/jmupdf/1.0/" dans le repository local Maven. 
2- copier la library  jmupdf-1.0.jar du repertoire lib à la racine du projet dans le repertoire créé à l'étape précédente
3- créer un configuration d'éxecution maven avec "package" comme commande à éxecuter
4- executer cette commande; deux fichiers .jar sont crées dans le dossier target, c'est celui ayant le nom 
   Visionneuse-0.0.1-SNAPSHOT-jar-with-dependencies.jar qui est l'éxecutable.

Execution:
Pour pouvoir s'exécuter le fichier .jar doit se trouver dans le même repertoire que la librairie dll correspondant au système
d'exploitation.
Copier la dll correspondante du repertoire lib à la racine du projet dans le repertoire contenant le .jar executable
Lancer le .jar