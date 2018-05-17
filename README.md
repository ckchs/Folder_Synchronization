# Folder_Synchronization
zapoctovy program

## Uzivatelska dokumentacia

### Argumenty

-s *cesta k zdrojovemu priecinku*

-t *cesta k cielovemu priecinku*

-rts **zapne mod real-time synchronizacie**

-backup **zapne mod zalohovania**

-type *typ synchronizacie*

-format *format nazvov priecinkov v zalohovacom mode*

-d *hlbka rekurzie*

-r **zapne neobmedzenu rekurziu**

-time *ako casto sa ma v real-time/backup synchronizacii vykonat synchronizacia (v sekundach)*


### Typy synchronizacie

*bwad* - BothWaysAskDiff obojsmerna synchronizacia v pripade rovnakeho nazvu sa cez konzolu vybere ktory subor sa ma prepisat, popripade sa zachovaju oba

*bwdtt* - BothWayDiffsToTarget obojsmerna synchronizacia v pripade rovnakeho nazvu sa prepise subor v cielovom priecinku

*bwdts* - BothWayDiffsToSource obojsmerna synchronizacia v pripade rovnakeho nazvu sa prepise subor v zdrojovom priecinku 

*bwnd* - BothWayNoDiffs obojsmerna synchronizacia v pripade rovnakeho nazvu sa nesynchronizuju

*sttwd* - SourceToTargetWithDiffs iba zo zdrojoveho do cieloveho priecinku, v pripade rovnakeho nazvu sa prepise v cielovom priecinku

*sttad* - SourceToTargetAskDiffs iba zo zdrojoveho do cieloveho priecinku, v pripade rovnakeho nazvu sa cez konzolu vybere ktory subor sa ma prepisat, popripade sa zachovaju oba

*sttnd* - SourceToTargetNoDiffs iba zo zdrojoveho do cieloveho priecinku, v pripade rovnakeho nazvu sa nesynchronizuju
 
*ttswd* - TargetToSourceWithDiffs iba z cieloveho do zdrojoveho, v pripade rovnakeho nazvu sa prepise v zdrojovom priecinku

*ttsad* - TargetToSourceAskDiffs iba z cieloveho do zdrojoveho, v pripade rovnakeho nazvu sa cez konzolu vybere ktory subor sa ma prepisat, popripade sa zachovaju oba

*ttsnd* - TargetToSourceNoDiffs iba z cieloveho do zdrojoveho, v pripade rovnakeho nazvu sa nesynchronizuju

### Obycajna synchronizacia

Priklady pouzitia:
z priecinku folder1 do folder2 ak sa nachadzaju subory s rovnakym nazvom spyta sa co spravit

	-s \folder1 -t \folder2 -r -type sttad
	
priecinky folder1 a folder2, obojsmerna synchronizacia s rekurziou do hlbky 2, v pripade rovnakeho nazvu sa prepise subor vo folder 2

	-s \folder1 -t \folder2 -d 2 bwdtt

### Real-Time synchronizacia
Kazdu minutu prebehne synchronizacia (nastavitelne pomocou -t)

Priklady pouzitia:
z priecinku folder1 do folder2 kazde 2 minuty sa obojsmerne synchronizuje ak je subor s rovnakym nazvom v oboch priecinkoch tak sa zachovaju oba

	-s \folder1 -t \folder2 -r -type bwnd -rts -time 120
	
### Zalohovanie
Kazdu minutu prebehne synchronizacia (nastavitelne pomocou -t)
Format v tvare [SimpleDateFormat](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html)

Priklady pouzitia:
priecinok folder1 zalohuje do folder2, kazde 3 minuty a format bude v tvare 01011970000000

	-s \folder1 -t \folder2 -r -time 180 -backup -format ddMMyyyyHHmmss






