/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.config.reflect;

import java.time.LocalDateTime;
import com.mycompany.pathfinder.config.DatabaseConnection;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;
/**
 *
 * @author HP
 */



public class ReflectUtility {

    private ReflectUtility() {
    }
 
    private static String toSnakeCase(String name) {
        return name
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    private static String toCamelCase(String name) {

        StringBuilder result = new StringBuilder();
        boolean upper = false;

        for (char c : name.toCharArray()) {

            if (c == '_') {
                upper = true;
            } else if (upper) {
                result.append(Character.toUpperCase(c));
                upper = false;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    
    // Récupérer tous les champs
   
    private static List<Field> getFields(Class<?> clazz) {

        List<Field> fields = new ArrayList<>();

        while (clazz != null && clazz != Object.class) {

            fields.addAll(
                    Arrays.asList(clazz.getDeclaredFields())
            );

            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    // OBJET -> MAP
    
    private static Map<String, Object> objectToMap(Object object) {

        Map<String, Object> data = new LinkedHashMap<>();

        for (Field field : getFields(object.getClass())) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            try {

                Object value = field.get(object);

                if (value == null) {
                    continue;
                }

                String columnName =
                        toSnakeCase(field.getName());

                data.put(columnName, value);

            } catch (IllegalAccessException e) {

                throw new RuntimeException(
                        "Erreur Reflection sur : "
                        + field.getName(),
                        e
                );
            }
        }

        return data;
    }

  
    // PARAMETRE SQL
    // Gestion des ENUM PostgreSQL

    private static void setPreparedValue(
            PreparedStatement statement,
            int index,
            Object value
    ) throws SQLException {

        if (value instanceof Enum<?>) {

            statement.setObject(
                    index,
                    value.toString(),
                    Types.OTHER
            );

        } else if (value instanceof LocalDateTime) {

            statement.setTimestamp(
                    index,
                    Timestamp.valueOf(
                            (LocalDateTime) value
                    )
            );

        } else {

            statement.setObject(index, value);
        }
    }

    // INSERT

    public static int insert(
            Object object,
            String tableName
    ) {

        Map<String, Object> data =
                objectToMap(object);

        // IDs générés automatiquement
        data.remove("id");
        data.remove("grid_id");
        data.remove("cell_id");
        data.remove("run_id");
        data.remove("path_cell_id");

        if (data.isEmpty()) {

            throw new IllegalArgumentException(
                    "Aucune donnée à insérer."
            );
        }

        String columns =
                String.join(", ", data.keySet());

        String placeholders =
                String.join(
                        ", ",
                        Collections.nCopies(
                                data.size(),
                                "?"
                        )
                );

        String sql =
                "INSERT INTO " + tableName
                + " (" + columns + ")"
                + " VALUES (" + placeholders + ")";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            int index = 1;

            for (Object value : data.values()) {

                setPreparedValue(
                        statement,
                        index++,
                        value
                );
            }

            return statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur INSERT dans "
                    + tableName
                    + " : "
                    + e.getMessage(),
                    e
            );
        }
    }

    
    // INSERT + récupération de l'ID
  
    public static int insertAndGetId(
            Object object,
            String tableName,
            String idColumn
    ) {

        Map<String, Object> data =
                objectToMap(object);

        data.remove(idColumn);

        if (data.isEmpty()) {

            throw new IllegalArgumentException(
                    "Aucune donnée à insérer."
            );
        }

        String columns =
                String.join(", ", data.keySet());

        String placeholders =
                String.join(
                        ", ",
                        Collections.nCopies(
                                data.size(),
                                "?"
                        )
                );

        String sql =
                "INSERT INTO " + tableName
                + " (" + columns + ")"
                + " VALUES (" + placeholders + ")"
                + " RETURNING " + idColumn;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            int index = 1;

            for (Object value : data.values()) {

                setPreparedValue(
                        statement,
                        index++,
                        value
                );
            }

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(idColumn);
                }
            }

            throw new RuntimeException(
                    "Impossible de récupérer l'ID généré."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur INSERT dans "
                    + tableName
                    + " : "
                    + e.getMessage(),
                    e
            );
        }
    }
    // SELECT

    public static <T> List<T> select(
            String tableName,
            Class<T> clazz
    ) {

        return select(
                tableName,
                null,
                clazz
        );
    }

    // SELECT AVEC CONDITION
   
    public static <T> List<T> select(
            String tableName,
            String condition,
            Class<T> clazz
    ) {

        List<T> result =
                new ArrayList<>();

        String sql =
                "SELECT * FROM " + tableName;

        if (condition != null
                && !condition.trim().isEmpty()) {

            sql += " WHERE " + condition;
        }

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            ResultSetMetaData metaData =
                    resultSet.getMetaData();

            int columnCount =
                    metaData.getColumnCount();

            while (resultSet.next()) {

                T object =
                        clazz.getDeclaredConstructor()
                             .newInstance();

                for (int i = 1;
                     i <= columnCount;
                     i++) {

                    String columnName =
                            metaData.getColumnName(i);

                    Object value =
                            resultSet.getObject(i);

                    String fieldName =
                            toCamelCase(columnName);

                    setField(
                            object,
                            fieldName,
                            value
                    );
                }

                result.add(object);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur SELECT dans "
                    + tableName
                    + " : "
                    + e.getMessage(),
                    e
            );
        }

        return result;
    }

    // SELECT PAR ID
   
    public static <T> T selectById(
            String tableName,
            String idColumn,
            Object id,
            Class<T> clazz
    ) {

        String sql =
                "SELECT * FROM "
                + tableName
                + " WHERE "
                + idColumn
                + " = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setObject(1, id);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    T object =
                            clazz.getDeclaredConstructor()
                                 .newInstance();

                    ResultSetMetaData metaData =
                            resultSet.getMetaData();

                    int columnCount =
                            metaData.getColumnCount();

                    for (int i = 1;
                         i <= columnCount;
                         i++) {

                        String columnName =
                                metaData.getColumnName(i);

                        Object value =
                                resultSet.getObject(i);

                        String fieldName =
                                toCamelCase(columnName);

                        setField(
                                object,
                                fieldName,
                                value
                        );
                    }

                    return object;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur SELECT BY ID : "
                    + e.getMessage(),
                    e
            );
        }

        return null;
    }

    // DATABASE -> OBJET
    
    private static void setField(
            Object object,
            String fieldName,
            Object value
    ) throws Exception {

        Field field = null;

        Class<?> clazz =
                object.getClass();

        while (clazz != null) {

            try {

                field =
                        clazz.getDeclaredField(
                                fieldName
                        );

                break;

            } catch (NoSuchFieldException e) {

                clazz = clazz.getSuperclass();
            }
        }

        if (field == null) {
            return;
        }

        field.setAccessible(true);

        if (value == null) {
            return;
        }

        Class<?> type =
                field.getType();

        // ENUM
        if (type.isEnum()) {

            @SuppressWarnings("unchecked")
            Class<? extends Enum> enumClass =
                    (Class<? extends Enum>) type;

            Object enumValue =
                    Enum.valueOf(
                            enumClass,
                            value.toString()
                    );

            field.set(
                    object,
                    enumValue
            );

            return;
        }

        // LocalDateTime
        if (type == LocalDateTime.class) {

            if (value instanceof Timestamp) {

                field.set(
                        object,
                        ((Timestamp) value)
                                .toLocalDateTime()
                );

                return;
            }
        }

        // int / Integer
        if (type == int.class
                || type == Integer.class) {

            field.set(
                    object,
                    ((Number) value).intValue()
            );

            return;
        }

        // long / Long
        if (type == long.class
                || type == Long.class) {

            field.set(
                    object,
                    ((Number) value).longValue()
            );

            return;
        }

        // double / Double
        if (type == double.class
                || type == Double.class) {

            field.set(
                    object,
                    ((Number) value).doubleValue()
            );

            return;
        }

        // float / Float
        if (type == float.class
                || type == Float.class) {

            field.set(
                    object,
                    ((Number) value).floatValue()
            );

            return;
        }

        // boolean / Boolean
        if (type == boolean.class
                || type == Boolean.class) {

            field.set(
                    object,
                    value
            );

            return;
        }

        field.set(
                object,
                value
        );
    }

   
    // UPDATE
  
    public static int update(
            Object object,
            String tableName,
            String idColumn
    ) {

        Map<String, Object> data =
                objectToMap(object);

        Object id =
                data.remove(idColumn);

        if (id == null) {

            throw new IllegalArgumentException(
                    "L'ID est obligatoire pour UPDATE."
            );
        }

        if (data.isEmpty()) {
            return 0;
        }

        StringBuilder set =
                new StringBuilder();

        for (String column : data.keySet()) {

            if (set.length() > 0) {
                set.append(", ");
            }

            set.append(column)
               .append(" = ?");
        }

        String sql =
                "UPDATE " + tableName
                + " SET " + set
                + " WHERE " + idColumn
                + " = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            int index = 1;

            for (Object value : data.values()) {

                setPreparedValue(
                        statement,
                        index++,
                        value
                );
            }

            statement.setObject(
                    index,
                    id
            );

            return statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur UPDATE dans "
                    + tableName
                    + " : "
                    + e.getMessage(),
                    e
            );
        }
    }

  
    // DELETE
   
    public static int delete(
            String tableName,
            String idColumn,
            Object id
    ) {

        String sql =
                "DELETE FROM "
                + tableName
                + " WHERE "
                + idColumn
                + " = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setObject(
                    1,
                    id
            );

            return statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur DELETE dans "
                    + tableName
                    + " : "
                    + e.getMessage(),
                    e
            );
        }
    }
}
