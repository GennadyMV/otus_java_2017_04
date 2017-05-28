package mygson;

import com.google.gson.Gson;
import mygson.testData.ArrayObjectType;
import mygson.testData.ArrayType;
import mygson.testData.Boxed;
import mygson.testData.Primitives;
import mygson.testData.TestNested;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sergey
 *         created on 28.05.17.
 */
public class ObjectWriterTest {
    @Test
    public void toJsonPrimitive() throws Exception {

        final Primitives obj = new Primitives();

        final Gson gson = new Gson();
        final String jsonExpected = gson.toJson(obj);

        final ObjectWriter writer = new ObjectWriter();
        final String jsonFact = writer.toJson(obj);
        assertEquals("primitives:", jsonExpected, jsonFact);
    }

    @Test
    public void toJsonNestedClass() throws Exception {

        final TestNested obj = new TestNested();

        final Gson gson = new Gson();
        final String jsonExpected = gson.toJson(obj);

        final ObjectWriter writer = new ObjectWriter();
        final String jsonFact = writer.toJson(obj);
        System.out.println("jsonExpected:" + jsonExpected);
        System.out.println("jsonFact:" + jsonFact);
        assertEquals("nestedClass:", jsonExpected, jsonFact);
    }

    @Test
    public void toJsonBoxed() throws Exception {

        final Boxed obj = new Boxed();

        final Gson gson = new Gson();
        final String jsonExpected = gson.toJson(obj);

        final ObjectWriter writer = new ObjectWriter();
        final String jsonFact = writer.toJson(obj);
        System.out.println("jsonExpected:" + jsonExpected);
        System.out.println("jsonFact:" + jsonFact);
        assertEquals("nestedClass:", jsonExpected, jsonFact);
    }

    @Test
    public void toJsonArray() throws Exception {

        final ArrayType obj = new ArrayType();

        final Gson gson = new Gson();
        final String jsonExpected = gson.toJson(obj);

        final ObjectWriter writer = new ObjectWriter();
        final String jsonFact = writer.toJson(obj);
        System.out.println("jsonExpected:" + jsonExpected);
        System.out.println("jsonFact:" + jsonFact);
        assertEquals("nestedClass:", jsonExpected, jsonFact);
    }

    @Test
    public void toJsonArrayObject() throws Exception {

        final ArrayObjectType obj = new ArrayObjectType();

        final Gson gson = new Gson();
        final String jsonExpected = gson.toJson(obj);

        final ObjectWriter writer = new ObjectWriter();
        final String jsonFact = writer.toJson(obj);
        System.out.println("jsonExpected:" + jsonExpected);
        System.out.println("jsonFact:" + jsonFact);
        assertEquals("nestedClass:", jsonExpected, jsonFact);
    }

}