package model.game;

import model.DatabaseConnection;
import model.GameView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GamesService {

    public static void selectAll(List<Games> targetList, DatabaseConnection database) {
        PreparedStatement statement = database.newStatement( "SELECT * FROM game ORDER BY game_ID" );

        try {
            if (statement != null) {

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add( new Games( results.getInt( "game_ID" ) , results.getString( "date_Played" ) ,
                                results.getString( "white_player" ) , results.getString( "black_player" ) ,
                                results.getString( "result" ) ) );
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }

    }

    private static Games selectById(int id , DatabaseConnection database) {
        Games result = null;

        PreparedStatement statement = database.newStatement( "SELECT * FROM game WHERE game_ID = ?" );

        try {
            if ( statement != null ) {

                statement.setInt( 1 , id );
                ResultSet results = database.executeQuery( statement );

                if ( results != null ) {
                    result = new Games(
                            results.getInt( "game_ID" ) ,
                            results.getString( "date_Played" ) ,
                            results.getString( "white_player" ) ,
                            results.getString( "black_player" ) ,
                            results.getString( "result" ) );
                }
            }
        } catch ( SQLException resultsException ) {
            return null;
        }

        return result;
    }
    public static void save(Games itemToSave, DatabaseConnection database) {

        Games existingItem = null;
        if (itemToSave.getGameId() != 0) existingItem = selectById(itemToSave.getGameId(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement(
                        "INSERT INTO game (date_Played, white_player, black_player, result) VALUES (?,?,?,?)" );
                statement.setString(1, itemToSave.getGameDate());
                statement.setString(2, itemToSave.getWhite());
                statement.setString(3, itemToSave.getBlack());
                statement.setString( 4 , itemToSave.getResult() );
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement(
                        "UPDATE game SET date_Played = ?, white_player = ?, black_player = ?, result = ? WHERE game_ID = ?" );
                statement.setString( 1 , itemToSave.getGameDate() );
                statement.setString(2, itemToSave.getWhite());
                statement.setString( 3 , itemToSave.getBlack() );
                statement.setString( 4 , itemToSave.getResult() );
                statement.setInt( 5 , itemToSave.getGameId() );
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }


    }	// insert & update

    public static void deleteById(int id , DatabaseConnection database) {
        PreparedStatement statement = database.newStatement( "DELETE FROM game WHERE game_ID = ?" );

        try {
            statement.setInt( 1 , id );
            database.executeUpdate( statement );
        } catch ( SQLException resultsException ) {
            System.out.println( "Database deletion error: " + resultsException.getMessage() );
        }

    }

    public static void selectForTable(List<GameView> targetList , DatabaseConnection database) {
        PreparedStatement statement = database.newStatement( "SELECT * From game ORDER BY game_ID" );

        try {
            if ( statement != null ) {

                ResultSet results = database.executeQuery( statement );

                if ( results != null ) {
                    while ( results.next() ) {
                        targetList.add( new GameView(
                                results.getInt( "game_ID" ) ,
                                results.getString( "date_Played" ) ,
                                results.getString( "white_player" ) ,
                                results.getString( "black_player" ) ,
                                results.getString( "result" ) ) );
                    }
                }
            }
        } catch ( SQLException resultsException ) {
            System.out.println( "Database select all error: " + resultsException.getMessage() );
        }
    }

}