

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
	Singe Int,
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