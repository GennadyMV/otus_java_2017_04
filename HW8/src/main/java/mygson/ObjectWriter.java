package mygson;

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
                Object value = field.get(obj);
                final String type = value.getClass().getComponentType().getCanonicalName();
                switch (type) {
                    case "byte":
                        byte[] byteValue = (byte[]) value;
                        for (int i = 0; i < byteValue.length; i++) {
                             if (i > 0) {
                                 sb.append(",");
                             }
                             sb.append(byteValue[i]);
                        }
                        return sb.toString();
                    case "short":
                        short[] shortValue = (short[]) value;
                        for (int i = 0; i < shortValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(shortValue[i]);
                        }
                        return sb.toString();
                    case "int":
                        int[]  intValue = (int[]) value;
                        for (int i = 0; i < intValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(intValue[i]);
                        }
                        return sb.toString();
                    case "long":
                        long[] longValue = (long[]) value;
                        for (int i = 0; i < longValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(longValue[i]);
                        }
                        return sb.toString();
                    case "float":
                        float[] floatValue = (float[]) value;
                        for (int i = 0; i < floatValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(floatValue[i]);
                        }
                        return sb.toString();
                    case "double":
                        double[] doubleValue = (double[]) value;
                        for (int i = 0; i < doubleValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(doubleValue[i]);
                        }
                        return sb.toString();
                    case "boolean":
                        boolean[] booleanValue = (boolean[]) value;
                        for (int i = 0; i < booleanValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(booleanValue[i]);
                        }
                        return sb.toString();
                    case "char":
                        char[] charValue = (char[]) value;
                        for (int i = 0; i < charValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append(charValue[i]);
                        }
                        return sb.toString();
                    case "java.lang.String":
                        String[] stringValue = (String[]) value;
                        for (int i = 0; i < stringValue.length; i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append("\"").append(stringValue[i]).append("\"");
                        }
                        return sb.toString();
                    default:
                        Object[] objectValue = (Object[]) value;
                        for (int i = 0; i < objectValue.length; i++) {
                            if (i > 0) {
                               sb.append(",");
                            }

                            if (isPrimitive(field)) {
                                sb.append(objectValue[i]);
                            } else {
                                sb.append(OPEN);
                                sb.append(processFields(objectValue[i]));
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
