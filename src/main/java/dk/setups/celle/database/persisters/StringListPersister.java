package dk.setups.celle.database.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StringListPersister extends StringType {

    private static final String SEPARATOR = ";";
    private static final StringListPersister singleton = new StringListPersister();

    private StringListPersister() {
        super(SqlType.STRING, new Class<?>[] { List.class });
    }

    @SuppressWarnings({"MayBeLombokGenerated", "unused"})
    public static StringListPersister getSingleton() {
        return singleton;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) javaObject;
        return String.join(SEPARATOR, list);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        String[] strings = ((String) sqlArg).split(SEPARATOR);
        return Arrays.asList(strings);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }
}