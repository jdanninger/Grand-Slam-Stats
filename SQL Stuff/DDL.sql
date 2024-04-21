

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


CREATE INDEX fn_indx ON Players (Firstname);
CREATE INDEX ln_indx ON Players (Lastname);

CREATE INDEX hitter_indx ON Hitters (Player_ID);
CREATE INDEX pithcer_indx ON Pitchers (Player_ID); -- added index to make pitcher/hitter search faster for stats

-- UseCase1 Stored Procedure
GO
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


-- UseCase2 Stroed Porcedure
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
    INSERT INTO Hitters (Game_ID, Player_ID, At_bats, Single, [Doubles], Triple, Home_runs, Balls)
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
    SET At_bats = @At_bats, Single = @Singles, [Doubles] = @Doubles, 
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

Create Procedure AddTeam
	@Name Varchar(50),
	@Divison Varchar(50)
AS
Begin
	Insert into Teams (Name, Divison_ID)
	VALUES (@Name, (Select ID from Divisons where name = @Divison));
End
GO

Create Procedure UpdateTeam
	@Name Varchar(50),
	@Division Varchar(50),
	@Original_Name Varchar(50)
As
Begin
	UPDATE Teams
	SET name = @Name, Divison_ID = (Select ID from Divisons where name = @Division)
	Where name = @Original_Name
END
go

Create Procedure deleteTeam
	@Name Varchar(50)
AS
Begin
	Delete From Teams
	Where name = @Name
End
GO

Create Procedure AddArena
	@Name Varchar(50),
	@OwningTeam Varchar(50),
	@Capacity int
AS
Begin
	Insert Into Arenas (Name, Owning_Team,Capacity)
	VALUES (@Name, (Select ID from Teams where name = @OwningTeam), @Capacity);
End
GO

Create Procedure UpdateArena
	@NewName Varchar(50),
	@Changable_Team Varchar(50),
	@NewCapacity Int,
	@OriginalName Varchar(50)
AS
Begin
	Update Arenas 
	SET Name = @NewName, 
		Owning_Team = (select ID from teams where name = @Changable_Team),
		Capacity = @NewCapacity
	Where name = @OriginalName
End
GO

Create Procedure deleteArena
	@Name Varchar(50)
AS
Begin
	Delete from Arenas
	Where name = @Name
End
GO

CREATE INDEX pid_indx ON Players (ID); -- index player ID since players are often searched by ID for modify and calculate stats
CREATE INDEX hitter_indx ON Hitters (Player_ID);
CREATE INDEX pithcer_indx ON Pitchers (Player_ID); -- added index to make pitcher/hitter search faster for stats

GO
CREATE PROCEDURE GetPlayerFullName
AS
BEGIN
    SELECT ID, CONCAT(Firstname, ' ', Lastname) AS full_name FROM Players;
END
GO

CREATE PROCEDURE CalculateSLG
    @PlayerID INT
AS
BEGIN
    SELECT 
        SUM(Single) AS TotalSingles,
        SUM(Doubles) AS TotalDoubles,
        SUM(Triple) AS TotalTriples,
        SUM(Home_runs) AS TotalHomeRuns,
        SUM(At_bats) AS TotalAtBats,
        SUM(Balls) AS TotalBalls,
        CASE 
            WHEN SUM(At_bats) = 0 THEN 0 
            ELSE (SUM(Single) + SUM(Doubles) * 2 + SUM(Triple) * 3 + SUM(Home_runs) * 4) / CAST(SUM(At_bats) AS FLOAT) 
        END AS SLG
    FROM 
        Hitters 
    WHERE 
        Player_ID = @PlayerID;
END
GO



CREATE PROCEDURE CalculatePitcherStats
    @PlayerID INT
AS
BEGIN
    SELECT 
        SUM(Base_on_balls) AS TotalWalks,
        SUM(Hits) AS TotalHits,
        SUM(Innings_Pitched) AS TotalInningsPitched,
        SUM(Strike_outs) AS TotalStrikeOuts
    FROM 
        Pitchers 
    WHERE 
        Player_ID = @PlayerID;
END
GO

CREATE PROCEDURE GetTeamNames
AS
BEGIN
    SELECT Name FROM Teams;
END;
GO

CREATE PROCEDURE InsertPlayer
    @FirstName NVARCHAR(50),
    @LastName NVARCHAR(50),
    @TeamName NVARCHAR(100)
AS
BEGIN
    DECLARE @TeamID INT

    -- Get the ID of the team based on the provided team name
    SELECT @TeamID = ID FROM Teams WHERE Name = @TeamName;

    -- Insert the player with the obtained team ID
    INSERT INTO Players (FirstName, LastName, Team_ID) VALUES (@FirstName, @LastName, @TeamID);
END
GO


CREATE PROCEDURE DeletePlayer
    @PlayerID INT
AS
BEGIN
    DELETE FROM Players WHERE ID = @PlayerID;
END
GO


