![GitHub Logo](/picsApp/combi.jpg)

Controllers:

mainActivity is het startscherm. Je kan hier naar chooseExisting players gaan of de hiscores bekijken. 

In choosePlayersActivity kan je zowel je de spelers kiezen (mogelijk ophalen in de activity chooseExistingPlayers, als er al spelers zijn opgeslagen), als de taal en mijn extra optie het aantal beginletters kiezen wat semi-random wordt gekozen voor de spelers voor het spel begint. (Spelers vinden dit hoop ik leuk om afwisseling te creëren, anders kan iemand steeds op dezelfde manier beginnen en waarschijnlijk zelfs geforceerd winnen). Nadat de namen en de opties ingevoerd zijn, kan begonnen worden met de game. 

In chooseExistingPlayerActivity staat een overzicht van de spelers waaruit je kan kiezen en kan je er een aanklikken om te kiezen. Daarnaast kan je de spelers deleten, dit om overnieuw te beginnen met de scores.

gameActivity, hierin vindt de game plaats, ook een schermpje popt up nadat de game klaar is met de winnaar en de reden waardoor de winnaar won. Een rematch kan dan besloten worden, of door naar de hiscores.

HiScoresActivity hier staan de hiscores en kan je alleen door/terug naar het main scherm.

Classes:

Game Activity handelt alle game aspecten af, hij maakt gebruik van de Dictionary class om te kijken of een speler af is en waarom. UNIEK voor mijn app is de mogelijkheid om semi-random letters te kiezen om mee te beginnen  Beginletters die woorden gekozen kunnen altijd een woord vormen en beginletters die in meer woorden uit het woordenboek voorkomen, worden vaker gekozen. Bij een rematch reset de Game zichzelf en de Dictionary. 

Dictionary class is een class waarin een woordenboek wordt ingeladen, en gefilterd wordt afhankelijk van de gekozen letters en of een woord in het huidige gefilterde woordenboek staat. Daarnaast is er een methode die geeft hoeveel woorden in het gefilterde woordenboek staan. UNIEK voor mijn app kan Dictionary ook een random woord kiezen. 

Player class is een class waarin een username wordt opgeslagen, dit om handig usernames te kunnen sorteren en de score te updaten na een winst of een speler toe te voegen aan de users.

PlayerFunctions is een class met functies geschreven om handig te werken met player lists/arrays. Voornamelijk wordt het gebruik om datastructuren van players om te zetten naar andere datastructuren.

UsersAdapter wordt gebruik om de usernames mooi te weergeven in een listview, deze adapter wordt gebruikt voor zowel de hi-scores als de existingplayersactivity.

Datastructuren:

Ik sla alle data die moet bestaan als de game wordt afgesloten op in sharedPreferences. De data die ik bedoel zijn de gekozen taal, aantal letters, en existing usernames. Bij gebrek aan beter heb ik de existing usernames en scores opgeslagen in een Stringset met de volgende vorm: “Player\nScore”, waarbij player de naam van de speler en score het aantal gewonnen potjes uitgedrukt in een getal. Ook sla ik bij beëindigen van game de data tijdelijk op in sharedpreferences. 

De enige datastructuur die ik niet heel standaard of voor de hand liggend vind, van degene die ik gebruik is de player Class, daarin staat een string met de naam en een integer met de score. Voordeel van hier een klasse voor te gebruiken is dat ik makkelijk een sorteer functie kon aanroepen i.p.v. hem zelf te schrijven. 


Code:

Ik heb mijn best gedaan om alles in zo duidelijk mogelijke functies te schrijven en ik vind dat alles nu redelijk vanzelfsprekend is. Ik hoop dat jullie het daar ook mee eens zijn.



