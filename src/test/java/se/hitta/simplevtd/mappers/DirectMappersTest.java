package se.hitta.simplevtd.mappers;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;


import se.hitta.simplevtd.SimpleVTD;
import se.hitta.simplevtd.SimpleVTDContext;
import se.hitta.simplevtd.mappers.MappersTest.Phone;
import se.hitta.simplevtd.mappers.MappersTest.PhoneMapper;

public class DirectMappersTest
{
    private static final SimpleVTD simpleVTD = new SimpleVTD();
    private static byte[] xml;

    @BeforeClass
    public static void before() throws IOException
    {
        xml = FileUtils.readFileToByteArray(new File(MappersTest.class.getClassLoader().getResource("test.xml").getFile()));
    }
    
    @Test
    public void canMapPhoneUsingDirectMapper() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        Phone phone = context.deserialize(new PhoneMapper(), "records/record/TELEPHONES/PHONE", null);

        assertEquals("131210", phone.netNumber);
    }
    
    @Test
    public void canMapPhoneCollectionUsingDirectMapper() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        Collection<Phone> phones = context.deserializeAll(new PhoneMapper(), "records/record/TELEPHONES/PHONE[AREACODE=08]");

        assertEquals(2, phones.size());
    }
}
