--UseCase1
{call AddGame(?, ?, ?, ?, ?, ?)}
{call ModifyGame(?, ?, ?, ?)}
{call DeleteGame(?)}

--UseCase2
{call AddHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}
{call ModifyHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}
{call DeleteHitterStats(?, ?)}

--Use case 3
EXEC GetPlayerFullName;
EXEC CalculateSLG @PlayerID = ?;
EXEC CalculatePitcherStats @PlayerID = ?;

-- USe case 4
Exec AddTeam ?,?
Begin Exec UpdateTeam ?,?,? End
Begin Exec deleteTeam ? End
Begin Exec AddArena ?,?,? End
Begin Exec UpdateArena ?,?,?,? End
Begin EXEC deleteArena ? End

-- Use case 5
EXEC GetTeamNames;
EXEC InsertPlayer @FirstName = ?, @LastName = ?, @TeamName = ?;
EXEC GetPlayerFullName;
EXEC DeletePlayer @PlayerID = ?

-- use case 6
Begin Exec AddLeague ?,? End
Begin Exec AddDivision ?,? End
Begin EXEC deleteLeague ? End
Begin Exec deleteDivision ? End
Begin Exec UpdateLeague ?,?,? End
Begin Exec UpdateDivision ?,?,? End
