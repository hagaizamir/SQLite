package hagai.edu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * An sqlite Data Base helper
 * <p>
 * craete a database
 * accesss the data base
 * modify the data base as necessary
 */

public class TodosDBHelper extends SQLiteOpenHelper {

    public TodosDBHelper(Context context) {
        super(context, TodosContract.DBName, null, version);
    }

    //WILL RUN ONCE , WHEN THE DATABASE IS FIRST CREATED
    @Override
    public void onCreate(SQLiteDatabase db) {
        //write sql script to create the tables
        db.execSQL("CREATE TABLE "+ TodosContract.TBL_Todos +"("+
                TodosContract.TBL_TODOS_COL_ID +"INTEGER PRIMARY KEY AUTOINCREMENT," +
                TodosContract.TBL_TODOS_COL_MISSION +"TEXT NOT NULL," +
                TodosContract.TBL_TODOS_COL_IMPORTANCE +"TEXT NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //modify the database as necessary

    }

    //a nice design pattern for a database
    public static class TodosContract {

        //the database name
        public static final String DBName = "Todos";

        //the current database version

        public static final int DBVersion = 1;

        public static final String TBL_Todos = "Todos";

        public static final String TBL_TODOS_COL_MISSION = "mission";

        public static final String TBL_TODOS_COL_IMPORTANCE = "importance";

        public static final String TBL_TODOS_COL_ID = "id";
    }


}
