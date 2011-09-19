package se.hitta.simplevtd.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import se.hitta.simplevtd.SimpleVTD;
import se.hitta.simplevtd.SimpleVTDContext;

public class MappersTest
{
    private static final SimpleVTD simpleVTD = new SimpleVTD();
    private static byte[] xml;

    @BeforeClass
    public static void before() throws IOException
    {
        simpleVTD.registerMapper(Phone.class, new PhoneMapper());
        xml = FileUtils.readFileToByteArray(new File(MappersTest.class.getClassLoader().getResource("test.xml").getFile()));
    }

    @Test
    public void canMapString() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        String expected = "1000656597";
        String actual = context.deserialize(String.class, "records/record/ID/value", StringUtils.EMPTY);

        assertEquals(expected, actual);
    }

    @Test
    public void willGetDefaultStringValue() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        String expected = "defval";
        String actual = context.deserialize(String.class, "records/record/ID/value/xxx", "defval");

        assertEquals(expected, actual);
    }

    @Test
    public void canMapStringAttribute() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        String expected = "VBOOLWARECOMPANIES";
        String expected2 = "1";
        String actual = context.deserialize(String.class, "records/@tablename", StringUtils.EMPTY);
        String actual2 = context.deserialize(String.class, "records/@from", StringUtils.EMPTY);

        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }

    @Test
    public void canMapInt() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        int expected = 1000656597;
        int actual = context.deserialize(Integer.class, "records/record/ID/value", 0);

        assertEquals(expected, actual);
    }

    @Test
    public void willGetDefaultIntValue() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        int expected = 23;
        int actual = context.deserialize(Integer.class, "records/record/ID/value/xxx", 23);

        assertEquals(expected, actual);
    }

    @Test
    public void canMapIntAttribute() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        int expected = 1;
        int actual = context.deserialize(Integer.class, "records/@from", 0);

        assertEquals(expected, actual);
    }

    @Test
    public void canMapLong() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        long expected = 1000656597;
        long actual = context.deserialize(Long.class, "records/record/ID/value", 0L);

        assertEquals(expected, actual);
    }

    @Test
    public void willGetDefaultLongValue() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        long expected = 123;
        long actual = context.deserialize(Long.class, "records/record/ID/value/xxx", 123L);

        assertEquals(expected, actual);
    }

    @Test
    public void canMapLongAttribute() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        long expected = 1;
        long actual = context.deserialize(Long.class, "records/@from", 0L);

        assertEquals(expected, actual);
    }

    @Test
    public void canMapDouble() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        double expected = 6581744.123;
        double actual = context.deserialize(Double.class, "records/record/COORD_NS", 0d);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void willGetDefaultDoublevalue() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        double expected = 744.123;
        double actual = context.deserialize(Double.class, "records/record/COORD_xxx", 744.123d);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void canMapDoubleAttribute() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        double expected = 100.001;
        double actual = context.deserialize(Double.class, "records/record/@score", 0d);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void canMapBoolean() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        boolean actual = context.deserialize(Boolean.class, "records/record/test", false);

        assertTrue(actual);
    }

    @Test
    public void willGetDefaultBooleanValue() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        boolean actual = context.deserialize(Boolean.class, "records/record/testxxx", true);

        assertTrue(actual);
    }

    @Test
    public void canMapBooleanAttribute() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        boolean actual = context.deserialize(Boolean.class, "records/record/test/@test", false);

        assertTrue(actual);
    }

    @Test
    public void canMapCollection() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        Collection<Phone> phones = context.deserializeAll(Phone.class, "records/record/TELEPHONES/PHONE");

        assertEquals(3, phones.size());
    }

    public static class Phone
    {
        public final String netNumber;

        public Phone(String netNumber)
        {
            this.netNumber = netNumber;
        }
    }

    public static class PhoneListMapper extends Mapper<Collection<Phone>>
    {

        @Override
        public Collection<Phone> deserialize(final SimpleVTDContext context, final Collection<Phone> defaultValue)
        {
            return Arrays.asList(new Phone(context.deserialize(String.class, "NETNUMBER", StringUtils.EMPTY)));
        }
    }

    public static class PhoneMapper extends Mapper<Phone>
    {
        @Override
        public Phone deserialize(SimpleVTDContext context, Phone defaultValue)
        {
            return new Phone(context.deserialize(String.class, "NETNUMBER", StringUtils.EMPTY));
        }
    }
}