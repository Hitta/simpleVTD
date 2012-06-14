package se.hitta.simplevtd.mappers;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ximpleware.VTDNav;

import se.hitta.simplevtd.SimpleVTD;
import se.hitta.simplevtd.SimpleVTDContext;
import se.hitta.simplevtd.mappers.MappersTest.Phone;
import se.hitta.simplevtd.mappers.MappersTest.PhoneMapper;

public class SimpleVTDContextCloning
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
    public void canCloneContext() throws Exception
    {
        SimpleVTDContext context = simpleVTD.createContext(xml);
        
        context.getNavigator().toElement(VTDNav.FIRST_CHILD, "records");
        
        SimpleVTDContext clonedContext = context.clone();
        clonedContext.getNavigator().toElement(VTDNav.FIRST_CHILD, "record");
        
        System.out.println(context.getNavigator().getCurrentIndex());
        System.out.println(clonedContext.getNavigator().getCurrentIndex());
        
        
        Collection<Phone> phones = context.deserializeAll(Phone.class, "record/TELEPHONES/PHONE");
        Collection<Phone> phones2 = clonedContext.deserializeAll(Phone.class, "TELEPHONES/PHONE");

        assertEquals(3, phones.size());
        assertEquals(3, phones2.size());
    }
}
