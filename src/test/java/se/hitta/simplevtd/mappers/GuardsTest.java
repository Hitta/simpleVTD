package se.hitta.simplevtd.mappers;

import static org.junit.Assert.assertEquals;

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

public class GuardsTest
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
    public void willFailWithDefaultValueIfGuardIsNotFound() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        String actual = context.deserialize(String.class, "records/record/guardsTest/guarded[@guard=test3]", "default value");

        assertEquals("default value", actual);
    }
    
    @Test
    public void willDeserializeCorrectElementBasedOnGuard() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        String actual = context.deserialize(String.class, "records/record/guardsTest/guarded[@guard=test2]", "default value");
        
        assertEquals("Åsa", actual);
    }
    
    @Test
    public void willDeserializeCorrectElementBasedOnGuard2() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        String actual = context.deserialize(String.class, "records/record/guardsTest[@guard=test3]/guarded[@guard=test]", "default value");
        
        assertEquals("Marie", actual);
    }
    
    @Test
    public void willDeserializeCorrectElementBasedOnGuard3() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        String actual = context.deserialize(String.class, "records/record/guardsTest[guarded=Ove]/guarded[@guard=test]", "default value");
        
        assertEquals("Marie", actual);
    }
    
    @Test
    public void canMapCollectionWithGuard() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        Collection<Phone> phones = context.deserializeAll(Phone.class, "records/record/TELEPHONES/PHONE[AREACODE=08]");

        assertEquals(2, phones.size());
    }
    
    @Test
    public void canMapCollectionWithGuard2() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        Collection<Phone> phones = context.deserializeAll(Phone.class, "records/record/TELEPHONES/PHONE[@guard=test]");

        assertEquals(2, phones.size());
    }
    
    @Test
    public void canMapCollectionWithGuard3() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);

        Collection<String> guardedNames = context.deserializeAll(String.class, "records/record/guardsTest[@guard=test3]/guarded");

        assertEquals(3, guardedNames.size());
    }
}
