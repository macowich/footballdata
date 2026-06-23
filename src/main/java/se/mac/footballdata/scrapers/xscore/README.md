# spara resultatfil
Gå till:
https://www.xscores.com/soccer/sweden/superettan/superettan/results
https://www.xscores.com/soccer/sweden/ettan/ettan-norra/results
Ctrl S spara i C:\data\xscore\results


# Databas
Ta bort alla med leagueId = 1000
db.events.deleteMany({'leagueId':1000})

Ta bort hela collection
db.odds.drop()
db.events.drop()





