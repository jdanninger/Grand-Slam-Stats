
INSERT Into Leagues (Name, Start_date)
Values ('Major League Baseball', '2008-11-11');

Insert into Divisons(Name, League_id)
Values('National League', (Select ID From Leagues where Name = 'Major League Baseball')) 

Insert into Teams(Name, Divison_ID)
values('Nationals',  (Select ID From Divisons where Name = 'National League'));

Insert into Teams(Name, Divison_ID)
values('Phillies',  (Select ID From Divisons where Name = 'National League'));

Insert into Teams(Name, Divison_ID)
values('Mets',  (Select ID From Divisons where Name = 'National League'));

Insert into Teams(Name, Divison_ID)
values('Braves',  (Select ID From Divisons where Name = 'National League'));

Insert into Teams(Name, Divison_ID)
values('Marlins',  (Select ID From Divisons where Name = 'National League'));

Insert into Players(Firstname, Lastname, Team_ID)
values('Juan', 'Soto', (Select ID From Teams where Name = 'Nationals'));

Insert into Players(Firstname, Lastname, Team_ID)
values('CJ', 'Abrams', (Select ID From Teams where Name = 'Nationals'));


Insert into Players(Firstname, Lastname, Team_ID)
values('Zack', 'Wheeler', (Select ID From Teams where Name = 'Phillies'));

Insert into Players(Firstname, Lastname, Team_ID)
values('Edmundo', 'Sosa', (Select ID From Teams where Name = 'Phillies'));

Insert into Seasons (Year, League_id)
values ('2023',  (Select ID From Leagues where Name = 'Major League Baseball'));

Insert into Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score)
values(1,1, 2, 8, 7);

Insert into Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score)
values(1,1, 2, 2, 4);

Insert into Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score)
values(1,2, 1, 11, 3);

Insert into Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score)
values(1,2, 1, 1, 2);

Insert into Hitters (Game_ID, Player_ID, At_bats, Singe, Doubles, Triple, Home_runs, balls)
values(1, 1, 5, 1, 0, 0, 0, 0);

Insert into Hitters (Game_ID, Player_ID, At_bats, Singe, Doubles, Triple, Home_runs, balls)
values(1, 2, 5, 1, 0, 0, 0, 1);

Insert into Hitters (Game_ID, Player_ID, At_bats, Singe, Doubles, Triple, Home_runs, balls)
values(1, 3, 8, 1, 0, 0, 0, 1);

Insert into Hitters (Game_ID, Player_ID, At_bats, Singe, Doubles, Triple, Home_runs, balls)
values(1, 4, 4, 0, 1, 2, 0, 1);

Insert into Pitchers (Game_ID, Player_ID, Runs, Earned_Runs, Hits, Base_on_balls, Strike_outs, Innings_Pitched, Home_runs, Number_pitches)
values(1, 3, 8, 7, 8, 1, 3, 3.2, 0, 15);

Insert into Pitchers (Game_ID, Player_ID, Runs, Earned_Runs, Hits, Base_on_balls, Strike_outs, Innings_Pitched, Home_runs, Number_pitches)
values(1, 1, 16, 9, 6, 2, 7, 6, 1, 60);


Select * from Hitters;
Select * from Pitchers;
Select * from Games;
select * from Leagues;
select * from Divisons;
select * from Teams;
select * from Players;
Select * from Seasons;

