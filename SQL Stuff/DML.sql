--UseCase1
SELECT ID, YEAR(Year) AS Year FROM Seasons;
SELECT Name FROM Teams
SELECT ID FROM Teams WHERE Name = ?
INSERT INTO Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score, Date) VALUES (?, ?, ?, ?, ?, ?)
SELECT ID FROM Seasons WHERE YEAR(Year) = ?
SELECT ID FROM Games
UPDATE Games SET Home_Score = ?, Away_Score = ?, Date = ? WHERE ID = ?
DELETE FROM Games WHERE ID = ?



--UseCase2
SELECT ID FROM Games ORDER BY Date DESC
SELECT ID FROM Players ORDER BY ID
INSERT INTO Hitters (Game_ID, Player_ID, At_bats, Singe, [Double], Triple, Home_runs, Balls) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
UPDATE Hitters SET At_bats = ?, Single = ?, [Double] = ?, Triple = ?, Home_runs = ?, Balls = ? WHERE Game_ID = ? AND Player_ID = ?
DELETE FROM Hitters WHERE Game_ID = ? AND Player_ID = ?

