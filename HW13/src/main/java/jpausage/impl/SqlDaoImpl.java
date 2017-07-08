package jpausage.impl;

import jpausage.ParamsHandler;
import jpausage.ResultHandler;
import jpausage.SqlDao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sergey
 *         created on 04.06.17.
 */
public class SqlDaoImpl implements SqlDao {

    private final Connection connection;

    private String tableName = "";
    private List<Field> ids = new ArrayList<>();
    private List<Field> columnsValue = new ArrayList<>();
    private List<Field> columnsAll = new ArrayList<>();



    public SqlDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getStatus() throws SQLException {
        return this.connection.isValid(1) ? "valid": "inValid";
    }

    @Override
    public <T> T queryExecutor(String sql, ParamsHandler params, ResultHandler<T> handler) throws Exception {
        T result;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                params.setParams(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                result = handler.handle(rs);
            }
        }
        return result;
    }

    @Override
    public void executeUpdate(String sql, ParamsHandler params) throws Exception {
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                params.setParams(ps);
            }
            ps.execute();
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }

    private void analizeObject(Object object) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = object.getClass();
        Annotation[] annotations = clazz.getAnnotations();

        boolean hasEntity = false;
        for (Annotation annotation: annotations) {
            if (annotation.annotationType().equals(Entity.class)) {
                hasEntity = true;
            }
            if (annotation.annotationType().equals(Table.class)) {
                tableName = ((Table) annotation).name();
            }
        }

        if (!hasEntity) {
            throw new IllegalArgumentException("Entity annotation not found");
        }

        if (tableName.equals("")) {
            throw new IllegalArgumentException("Table name not found");
        }

        for(Field field: object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                columnsAll.add(field);
                if (getFieldValue(field,object) != null) {
                    columnsValue.add(field);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    ids.add(field);
                }
            }
        }
    }

    @Override
    public void save(Object object) throws Exception {
        analizeObject(object);

        final String insert = makeInsertSql();
        executeUpdate(insert, ps -> {
            int columnIdx = 0;
            for (final Field field : columnsValue) {
                final String type = field.getType().getTypeName();
                try {
                    Object value = getFieldValue(field, object);
                    if (value != null) {
                        switch (type) {
                            case "java.lang.Long":
                                ps.setLong(++columnIdx, (Long) value);
                                break;
                            case "java.lang.String":
                                ps.setString(++columnIdx, (String) value);
                                break;
                            case "java.lang.Integer":
                                ps.setInt(++columnIdx, (int) value);
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported data type:" + type);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    @Override
    public void load(Object object, Map<String, Object> idValues) throws Exception {
        analizeObject(object);
        final String select = makeSelectSql();

        queryExecutor(select, ps -> {
            for (int idx = 0; idx < ids.size(); idx++) {
                final Field fieldId = ids.get(idx);
                final Class<?> type = fieldId.getType();
                try {
                    switch (type.getTypeName()) {
                        case "java.lang.Long":
                            ps.setLong(idx + 1, (Long) idValues.get(fieldId.getName()));
                            break;
                        case "java.lang.String":
                            ps.setString(idx + 1, (String) idValues.get(fieldId.getName()));
                            break;
                        case "java.lang.Integer":
                            ps.setInt(idx + 1, (Integer) idValues.get(fieldId.getName()));
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported data type:" + type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }

            }
        }, rs-> {
            while (rs.next()) {
                for (final Field field : columnsAll) {
                    final Class<?> type = field.getType();
                    try {
                        switch (type.getTypeName()) {
                            case "java.lang.Long":
                                setFieldValue(field, object, rs.getLong(field.getName()));
                                break;
                            case "java.lang.String":
                                setFieldValue(field, object, rs.getString(field.getName()));
                                break;
                            case "java.lang.Integer":
                                setFieldValue(field, object, rs.getInt(field.getName()));
                                break;
                            default:
                                   throw new IllegalArgumentException("Unsupported data type:" + type);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
            return null;
        });
    }

    private Object getFieldValue(Field field, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String fieldName = field.getName();
        final String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        Method method = object.getClass().getMethod(getterName);
        return method.invoke(object);
    }

    private void setFieldValue(Field field, Object object, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String fieldName = field.getName();
        final String getterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        Method method = object.getClass().getMethod(getterName, value.getClass());
        method.invoke(object, value);
    }

    private String makeInsertSql() {
        final StringBuilder sbPlaces = new StringBuilder();
        for (int idx = 0; idx < columnsValue.size(); idx++) {
            if (idx > 0) {
                sbPlaces.append(",");
            }
            sbPlaces.append("?");
        }


        final String insertSql = "insert into " + tableName +
                "(" + columnsValue.stream().map(Field::getName).collect(Collectors.joining(",")) + ")" +
                "values" + "(" + sbPlaces.toString() + ")";

        System.out.println("insert sql:" + insertSql);
        return insertSql;
    }

    private String makeSelectSql() {
        final StringBuilder sbIds = new StringBuilder();
        for (Field id : ids) {
            sbIds.append(" and ").append(id.getName()).append(" = ? \n");
        }

        final String selectSql = "select * from " + tableName +" where 1=1 \n" + sbIds.toString();

        System.out.println("select sql:" + selectSql);
        return selectSql;
    }

}
