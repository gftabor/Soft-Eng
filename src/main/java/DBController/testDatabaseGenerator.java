package DBController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

/**
 * Created by jasonashton on 4/8/17.
 */
public class testDatabaseGenerator extends databaseGeneratorAbstract{
    String dbName = "testDB";

    public String getDbName(){
        return dbName;
    }
}