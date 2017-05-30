package mygson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * @author sergey
 *         created on 28.05.17.
 */
public class ObjectWriter {

    private final StringBuilder result;
    private final static String OPEN = "{";
    private final static String CLOSE = "}";

    public ObjectWriter() {
        result = new StringBuilder();
        result.append(OPEN);
    }

    public String toJson(Object obj) throws IllegalAccessException {
        result.append(processFields(obj));
        return result.append(CLOSE).toString();
    }

    private String makePair(String name, Object value) {
        final String valueStr = value.getClass().getCanonicalName().equals("java.lang.String") ? "\"" + value + "\"" : value.toString();
        return "\"" + name + "\":" + valueStr;
    }

    private Object getValue(Field field, Object obj) {
        StringBuilder sb = new StringBuilder();
        try {
            field.setAccessible(true);
            if (field.getType().isArray()) {
                final Object value = field.get(obj);
                final String type = value.getClass().getComponentType().getCanonicalName();
                switch (type) {
                    case "byte":
                    case "short":
                    case "int":
                    case "long":
                    case "float":
                    case "double":
                    case "boolean":
                    case "char":
                        for (int i = 0; i < Array.getLength(value); i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(Array.get(value, i));
                        }
                        return sb.toString();
                    case "java.lang.String":
                        for (int i = 0; i < Array.getLength(value); i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append("\"").append(Array.get(value, i)).append("\"");
                        }
                        return sb.toString();
                    default:
                        for (int i = 0; i < Array.getLength(value); i++) {
                            if (i > 0) {
                               sb.append(",");
                            }

                            if (isPrimitive(field)) {
                                sb.append(Array.get(value, i));
                            } else {
                                sb.append(OPEN);
                                sb.append(processFields(Array.get(value, i)));
                                sb.append(CLOSE);
                            }
                        }
                        return sb.toString();
                }
            }
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private boolean isPrimitive(Field field) {
        if (field.getType().isPrimitive()) {
            return true;
        }

        final String[] splittedName = field.getType().getCanonicalName().split("\\.");
        if (splittedName.length == 3 && splittedName[0].equals("java") && splittedName[1].equals("lang") ) {
            return true;
        }
        return false;
    }

    private String processFields(Object obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int idx = 0; idx < fields.length; idx++) {
            if (idx > 0) {
                sb.append(",");
            }
            Field field = fields[idx];

            if (field.getType().isArray()) {
                sb.append("\"").append(field.getName()).append("\":");
                sb.append("[");
                sb.append(getValue(field, obj));
                sb.append("]");
            } else {
                if (isPrimitive(field)) {
                    sb.append(makePair(field.getName(), getValue(field, obj)));
                } else {
                    sb.append("\"").append(field.getName()).append("\":");
                    sb.append(OPEN);
                    sb.append(processFields(getValue(field, obj)));
                    sb.append(CLOSE);
                }

            }
        }
        return sb.toString();
    }
}
