

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

--Use Case 4 Add/Modify/Delete Teams and Arena
------Add Team
Create Procedure AddTeam
	@Name Varchar(50),
	@Divison Varchar(50)
AS
Begin
	Insert into Teams (Name, Divison_ID)
	VALUES (@Name, (Select ID from Divisons where name = @Divison));
End;

------ Update Team
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

------ Delete Team
Create Procedure deleteTeam
	@Name Varchar(50)
AS
Begin
	Delete From Teams
	Where name = @Name
End

----- AddArena
Create Procedure AddArena
	@Name Varchar(50),
	@OwningTeam Varchar(50),
	@Capacity int
AS
Begin
	Insert Into Arenas (Name, Owning_Team,Capacity)
	VALUES (@Name, (Select ID from Teams where name = @OwningTeam), @Capacity);
End

--------- UpdateArena
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

------- Delete Arena
Create Procedure deleteArena
	@Name Varchar(50)
AS
Begin
	Delete from Arenas
	Where name = @Name
End

--Use Case 6 Add/Delete/Modify Leagues and Divisions
----- Add a League
Create Procedure AddLeague
	@Name varchar(50),
	@Date date
AS
Begin
	Insert Into Leagues (Name, Start_Date)
	VALUES (@Name, @Date)
End

-----Add Divisions in League
Create Procedure AddDivision
	@Name Varchar(50),
	@League Varchar(50)
As
Begin
	Insert Into Divisons (Name, League_id)
	VALUES (@Name, (Select ID from Leagues where name = @League))
End

------- Delete Leagues
Create Procedure deleteLeague
	@Name Varchar(50)
AS
Begin
	Delete From Leagues
	Where name = @Name
End
------- Delete Divisions
Create Procedure deleteDivision
	@Name Varchar(50)
AS
Begin	
	Delete from Divisons
	Where name = @Name
End

----- Update Leagues
​​Create Procedure UpdateLeague
	@New_Name Varchar(50),
	@New_Date Date,
	@Original_Name Varchar(50)
AS
Begin
	Update Leagues
	SET name = @New_Name, Start_Date = @New_Date
	Where name = @Original_Name
End

------- Update Divisions
Create Procedure UpdateDivision
	@NewName Varchar(50),
	@Changable_League Varchar(50),
	@OriginalName Varchar(50)
AS
BEGIN
	Begin Transaction
	UPDATE Divisons
	SET name = @NewName, League_id = (Select ID from Leagues where name = @Changable_League)
	WHERE name = @OriginalName
	Commit Transaction
END

