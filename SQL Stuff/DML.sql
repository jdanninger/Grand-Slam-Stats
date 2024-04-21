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
--Procedure
CREATE PROCEDURE AddHitterStats
    @Game_ID INT,
    @Player_ID INT,
    @At_bats INT,
    @Single INT,
    @Double INT,
    @Triple INT,
    @Home_runs INT,
    @Balls INT
AS
BEGIN
    INSERT INTO Hitters (Game_ID, Player_ID, At_bats, Single, [Double], [Triple], Home_runs, Balls)
    VALUES (@Game_ID, @Player_ID, @At_bats, @Single, @Double, @Triple, @Home_runs, @Balls);
END

CREATE PROCEDURE ModifyHitterStats
    @Game_ID INT,
    @Player_ID INT,
    @At_bats INT,
    @Single INT,
    @Double INT,
    @Triple INT,
    @Home_runs INT,
    @Balls INT
AS
BEGIN
    UPDATE Hitters
    SET At_bats = @At_bats, Single = @Single, [Double] = @Double, [Triple] = @Triple, Home_runs = @Home_runs, Balls = @Balls
    WHERE Game_ID = @Game_ID AND Player_ID = @Player_ID;
END

CREATE PROCEDURE DeleteHitterStats
    @Game_ID INT,
    @Player_ID INT
AS
BEGIN
    DELETE FROM Hitters WHERE Game_ID = @Game_ID AND Player_ID = @Player_ID;
END
