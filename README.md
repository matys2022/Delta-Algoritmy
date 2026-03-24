Toto Java-based malování využívá knihovnu java.swing, která poskytuje základní prvky pro tvoření oken a vybarvování jednotlivých pixelů v daném okně do plátna. 


### Stavba
Já jsem trochu upravil architekturu, abych mohl využívat dvou vrstev, tedy render vrstvy a preview vrstvy. Do preview vrstvy se vykreslují objekty, které jsou momentálně vytvářeny nebo upravovány. Upravil jsem to tak, abych při každém překreslování jediného tvaru nemusel obnovit všechny už vytvořené objekty. 

Při každém překreslení render vrstvy se zároveň zavolá překreslení interface, který jsem si vytvořil vlastní. Proto je možné kreslit přes interface ve chvíli, kdy dochází k editaci či vytváření nového objektu.


### Interface
Interface funguje basically jako css. Máte tam padding, margin, je tam flex a box atd.. (Všechno jsem psal já, nikoliv chat.) Při přidání nebo změně velkosti/paddingu/marginu u child elementu se u jeho parent elementu zavolá metoda. Metoda refresh postupně provolá překreslení kontentu na všech parent elementech. 

Jsou zde i custom elementy, které mají možnost interakce; těmi jsou tlačítka, color selector a width selector. Tyto action elementy potřebují jako parametr lambda funkci, kterou při změně volají.

### Možnosti vykreslování
Interface má dva navbary. Navbar nahoře nabízí přehled různých tvarů, které je možné vykreslit. Kliknutím na ně uživatel aktivuje daný mód a může kreslit buďto stylem click - release && click - release nebo click - drag - release. Dalšími prvky jsou widthSelector a colorSelector. ColorSelector nabízí paletu barev, ze které si může uživatel vybrat.  WidthSelector zobrazuje aktuální šířku čáry a tlačítka pro snížení nebo zvýšení dané šířky.

### Možnosti úpravy
Navbar pod ním umožňuje změnit nástroj, tedy způsob, jakým uživatel manipuluje s již vykreslenými canvas elementy. Je zde možnost upravit jakýkoliv bod již vykresleného tvaru. U kružnice se tato změna projeví pouze na jejím poloměru. Je zde možnost přesouvat tvary, upravovat jejich vrcholy, smazat jediný objekt nebo ho i vyplnit. 

Vyplňování objektu funguje na bázi floodfillu, který nejdříve najde boundary body, které stanoví jako hranice pro vykreslení výplně. Boundary body jsou buďto hranice jiných objektů nebo hranice plátna. Při každém překreslení se už řídí pouze podle předdefinovaných hranic a nebere ohled na hranice ostatních objektů.


### Zkratky 
- `c` – vyčištění plátna a odstranění všech vykreslených objektů.
- `Shift` – snapping vykreslované čáry.
- `Ctrl` – vykreslení přerušované čáry.



