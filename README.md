Visionneuse PDF utilise la librairie Java jmupdf(licence GPLv3) qui elle m�me est bas�e sur mupdf(licence AGPL) 
Le repertoire lib se trouvant � la racine du projet contient les dll de mupdf et le .jar de jmupdf
Compilation:
C'est un projet Maven, pour exporter l'application en .jar suivez les �tapes suivantes
1- cr�er le repertoire "jmupdf-core/jmupdf/1.0/" dans le repository local Maven. 
2- copier la library  jmupdf-1.0.jar du repertoire lib � la racine du projet dans le repertoire cr�� � l'�tape pr�c�dente
3- cr�er un configuration d'�xecution maven avec "package" comme commande � �xecuter
4- executer cette commande; deux fichiers .jar sont cr�es dans le dossier target, c'est celui ayant le nom 
   Visionneuse-0.0.1-SNAPSHOT-jar-with-dependencies.jar qui est l'�xecutable.

Execution:
Pour pouvoir s'ex�cuter le fichier .jar doit se trouver dans le m�me repertoire que la librairie dll correspondant au syst�me
d'exploitation.
Copier la dll correspondante du repertoire lib � la racine du projet dans le repertoire contenant le .jar executable
Lancer le .jar