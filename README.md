Visionneuse PDF utilise la librairie Java jmupdf(licence GPLv3) qui elle m�me est bas�e sur mupdf(licence AGPL) 
Le repertoire lib se trouvant � la racine du projet contient les dll de mupdf et le .jar de jmupdf(dans le repertoire jmupdf-core)

Specification UPnP:
La visionneuse re�oit essentiellemnt deux commandes UPnP sous forme de chaine de caract�res:
1-DROITE: qui avance le fichier en lecture d'une page
2-GAUCHE: qui recule le fichier en lecture d'une page
Les autres commandes ne sont pas trait�es pour l'instant

Execution:
Le repertoire build contient les �l�ments n�cessaires pour �xecuter l'application: le fichier .jar et les dll
Il suffit de lancer le fichier .jar pour �xecuter l'application 

Maintenance:
C'est un projet Maven
la librairie jmupdf n'a pas de depot Maven, il faut donc l'ajouter manuelement
Pour cela il suffit de copier le repertoire "jmupdf-core" dans le repository local Maven. 
Effectuer les modifications � faire
Ajouter une configuration d'�xecution Maven avec la phase "package" pour exporter en .jar 
Executer cette commande; deux fichiers .jar sont cr�es dans un nouveau dossier target 
   Visionneuse-0.0.1-SNAPSHOT-jar-with-dependencies.jar qui est le bon �xecutable