--UseCase1
SELECT ID, YEAR(Year) AS Year FROM Seasons
SELECT Name FROM Teams
SELECT ID FROM Teams WHERE Name = ?
SELECT ID FROM Seasons WHERE YEAR(Year) = ?
SELECT ID FROM Games
{call AddGame(?, ?, ?, ?, ?, ?)}
{call ModifyGame(?, ?, ?, ?)}
{call DeleteGame(?)}

--UseCase2
SELECT ID FROM Games ORDER BY Date DESC
SELECT ID FROM Players ORDER BY ID
{call AddHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}
{call ModifyHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}
{call DeleteHitterStats(?, ?)}


