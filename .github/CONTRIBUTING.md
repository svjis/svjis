# Jak přispívat do projektu SVJIS

Děkuji, že zvažujete přispívat do projektu SVJIS. Níže uvádím několik způsbů jak můžete spolupracovat:

* přidáváním nového kódu;
* reportováním chyb;
* opravami chyb;
* vymýšlením nových vlastností systému;
* spoluprací na code review.

## Jak přidávat nový kód

Nejlepším způsobem pro nové přispěvovatele je použití GitHub pull requestů. To znamená vytvoření privátního forku projektu, zapracování změn do větve ve vašem forku a nakonec vytvoření pull requestu prostřednistvím GitHub UI (je to jednodušší než se zdá). Pull request je jednak způsob jak převzít kód z vaší větve do hlavního repozitáře a jednak je to framework pro code review.  

Tento projekt používá [Gitflow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow). Pojmenování větví ve vašem projektu by mělo respektovat konvenci používanou v hlavním projektu.  

*Pro přidání nové vlastnosti systému:*

* **feature/**meaningful-feature-name
  * např. `feature/component-error-handler`

*Pro opravu chyby:*

* **fix/**feature-name/short-name-of-problem-being-fixed
  * např. `fix/error-page-handler/parent-page-lookup`

Než začnete věnovat váš čas vlastnímu programování, je někdy lepší zamýšlenou úpravu nejprve prodiskutovat s komunitou. V tom případě vytvořte GitHub issue s popisem problému nebo vlastnosti kterou zamýšlíte řešit.  

Pokud už máte práva provádět commity, pak byste opravy a malé úpravy měli dělat přímo ve sdíleném repozitáři.  

Existuje hezký návod jak pracovat s pull requesty: [https://help.github.com/articles/using-pull-requests](https://help.github.com/articles/using-pull-requests). Tak jak je uvedeno v článku, používáme obojí **Fork & Pull** a **Shared Repository Model**.

### Než začnete programovat

Nejlepší pull requesty jsou ty malé a zaměřené na jeden konkrétní problém. Nepokoušejte se v jednom pull requestu "předělat svět"

* Nezapomeňte přidat JUnit test pro váš kód.

## Spolupráce na Code Review

Pokud nemáte čas programovat, tak můžete stále pomoci třeba s Code Review. V takovém případě se podívejte do menu Pull requests a můžete zde připomínkovat úpravy ostatních.
