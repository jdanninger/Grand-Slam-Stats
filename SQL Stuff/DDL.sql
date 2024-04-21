

CREATE TABLE Leagues(
	ID INT IDENTITY(1,1),
	Name Varchar(50) NOT NULL,
	Start_Date Date,
	PRIMARY KEY (ID)
);

CREATE TABLE Seasons (
	ID INT IDENTITY(1,1),
	Year date,
	League_id int NOT NULL,
	FOREIGN KEY (League_id) REFERENCES Leagues(ID) on delete cascade,
	PRIMARY KEY (ID)
);

CREATE TABLE Divisons (
	ID INT IDENTITY(1,1),
	Name Varchar(50),
	League_id int NOT NULL, 
	FOREIGN KEY (League_id) REFERENCES Leagues(ID) on delete cascade,
	PRIMARY KEY (ID)
);

CREATE TABLE Teams (
	ID INT IDENTITY(1,1),
	Name Varchar(50),
	Divison_ID int, 
	FOREIGN KEY (Divison_ID) REFERENCES Divisons(ID) on delete set null,
	PRIMARY KEY (ID)
);

CREATE TABLE Arenas (
	ID INT IDENTITY(1,1),
	Name Varchar(50) NOT NULL,
	Owning_Team INT,
	Capacity INT
	FOREIGN KEY (Owning_Team) REFERENCES Teams(ID) on delete set null,
	PRIMARY KEY (ID)
);

CREATE TABLE Players (
	ID INT IDENTITY(1,1),
	Firstname Varchar(50),
	Lastname Varchar(50) NOT NULL,
	Team_ID INT,
	FOREIGN KEY (Team_ID) REFERENCES Teams(ID) on delete set null,
	PRIMARY KEY (ID)
);

CREATE TABLE Games (
	ID INT IDENTITY(1,1),
	Season_ID INT,
	Home_Team INT NOT NULL,
	Away_Team INT NOT NULL,
	Home_Score INT,
	Away_Score INT,
	Date Date,
	FOREIGN KEY (Home_Team) REFERENCES Teams(ID) on delete cascade,
	FOREIGN KEY (Away_Team) REFERENCES Teams(ID) on delete no action,
	FOREIGN KEY (Season_ID) REFERENCES Seasons(ID) on delete cascade,
	PRIMARY KEY (ID)
);

CREATE TABLE Pitchers (
	Game_ID INT,
	Player_ID INT,
	Runs INT,
	Earned_Runs Int,
	Hits INT,
	Base_on_balls INT,
	Strike_outs INT,
	Innings_Pitched Numeric(7,1),
	Home_runs INT,
	Number_pitches INT,
	FOREIGN KEY (Game_ID) REFERENCES Games(ID) on delete cascade,
	FOREIGN KEY (Player_ID) REFERENCES Players(ID) on delete cascade,
	Primary Key (Game_ID, Player_ID)
);

CREATE TABLE Hitters (
	Game_ID INT,
	Player_ID INT,
	At_bats INT,
	Single INT,
	Doubles INT,
	Triple INT,
	Home_runs INT,
	balls INT,
	FOREIGN KEY (Game_ID) REFERENCES Games(ID) on delete cascade,
	FOREIGN KEY (Player_ID) REFERENCES Players(ID) on delete cascade,
	Primary Key (Game_ID, Player_ID)
);

CREATE LOGIN db_user WITH PASSWORD = 'ThisIsANewPassword123@';
USE GrandSlamStats;
CREATE USER db_user FOR LOGIN db_user;
ALTER ROLE db_owner ADD MEMBER db_user;




-- UseCase1 Stored Procedure
CREATE PROCEDURE AddGame
    @Season_ID INT,
    @Home_Team INT,
    @Away_Team INT,
    @Home_Score INT,
    @Away_Score INT,
    @Date DATE
AS
BEGIN
    INSERT INTO Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score, Date)
    VALUES (@Season_ID, @Home_Team, @Away_Team, @Home_Score, @Away_Score, @Date)
END
GO

-- Modifying Game
CREATE PROCEDURE ModifyGame
    @Game_ID INT,
    @Home_Score INT,
    @Away_Score INT,
    @Date DATE
AS
BEGIN
    UPDATE Games
    SET Home_Score = @Home_Score, Away_Score = @Away_Score, Date = @Date
    WHERE ID = @Game_ID
END
GO

-- Deleting Game
CREATE PROCEDURE DeleteGame
    @Game_ID INT
AS
BEGIN
    DELETE FROM Games WHERE ID = @Game_ID
END
GO


-- UseCase2 Stored Procedure
-- Adding Hitter Stats
CREATE PROCEDURE AddHitterStats
    @Game_ID INT,
    @Player_ID INT,
    @At_bats INT,
    @Singles INT,
    @Doubles INT,
    @Triples INT,
    @Home_runs INT,
    @Balls INT
AS
BEGIN
    INSERT INTO Hitters (Game_ID, Player_ID, At_bats, Single, [Double], Triple, Home_runs, Balls)
    VALUES (@Game_ID, @Player_ID, @At_bats, @Singles, @Doubles, @Triples, @Home_runs, @Balls)
END
GO

-- Modifying Hitter Stats
CREATE PROCEDURE ModifyHitterStats
    @Game_ID INT,
    @Player_ID INT,
    @At_bats INT,
    @Singles INT,
    @Doubles INT,
    @Triples INT,
    @Home_runs INT,
    @Balls INT
AS
BEGIN
    UPDATE Hitters
    SET At_bats = @At_bats, Single = @Singles, [Double] = @Doubles, 
        Triple = @Triples, Home_runs = @Home_runs, Balls = @Balls
    WHERE Game_ID = @Game_ID AND Player_ID = @Player_ID
END
GO

-- Deleting Hitter Stats
CREATE PROCEDURE DeleteHitterStats
    @Game_ID INT,
    @Player_ID INT
AS
BEGIN
    DELETE FROM Hitters WHERE Game_ID = @Game_ID AND Player_ID = @Player_ID
END
GO
