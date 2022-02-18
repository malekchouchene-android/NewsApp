# NewsApp
l'architecture de l'app est composée de 3 package :

  -ui: il contient les activitys les viewModels et les view en compose 
  
  -domaine : .il contient les entities (News ici) et les regles des ces entities (comme par exemple le titre d'une news ne peut pas etre null) 
              UseCase pour faire la jonction le ui et le repository . Même si le usecase est dans le cas de ce projet est un passe-plat ,
              il permet d'avoir une seul action puisque la view n'a pas besion de tout les methodes proposées par le repository 
              les interface des repository responsable d'avoir les données et qui expose des Flow
              
 -data: il contient les implémentations des repositories qui prenent en paramètres des datasource (ici une api) et aussi le mapping des données brutes de l'api en domaine.
 
 Donc en résumé on a un flow de données qui part du datasource vers le repository (faire l'appel ws transfomer les données ), ensuite vers le usecase et qui sera collecter dans le viewModel et enfin la vue compose 
 sera notifiée garce des StateFlow 
 
 Le Flow couplé à des  coroutines permet d'avoir des flux de donné manière réactif et de changer à tout moment les thread pour effectuer des operations hors le main thread. 
 Par rapport à l'RX, on  gagne en terme de cancelation puisque en les lançant dans le scope viewModelScope , ils seront relié au lifecycle du viewModel donc pas besion de faire dispoe à la main comme dans le Rx.
 de plus il fait partir du jetpack  pas besion de plus de code pour l'adapter à Android et enfin il presente moins d'operateurs que le Rx ce qui rend sa prise moins compliquée.
 Le choix de compose est sa simplicité et la rapidité de créer des vue avec .
 
 Pour l'injection de dépandance , koin est plus adapté  à ce type de projet que dagger 2. En effet , il y a moins de code à écrire que Dagger 2 et il est facile à mettre en place  malgrés sa lenteur relative au démarrage vu qu'il n'a y pas de code généré .
 
 Comme amélioration sur cette app , on peut gérer le mode offline en ajoutant un DiskLurCache et en metant son time to live . On peut gérer l'orientation en proposant une autre vue adaptée au landscape ...
 
