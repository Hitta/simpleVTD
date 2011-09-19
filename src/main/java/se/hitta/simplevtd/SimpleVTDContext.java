package se.hitta.simplevtd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.hitta.simplevtd.mappers.Mapper;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;

/**
 * Use SimpleVTDContext to deserialize XML into entities.<br>
 * A SimpleVTDContext is created using the factory method:
 * {@link SimpleVTD#createContext}
 */
public final class SimpleVTDContext
{
    private static final Logger log = LoggerFactory.getLogger(SimpleVTDContext.class);
    private final Map<Class<?>, Mapper<?>> mappers;
    private final VTDNav navigator;

    /**
     * User extras to store information that has to be passed with the context.
     */
    public final Map<String, Object> extras;

    SimpleVTDContext(final VTDNav navigator, final Map<Class<?>, Mapper<?>> mappers)
    {
        this.navigator = navigator;
        this.mappers = mappers;
        this.extras = new HashMap<String, Object>();
    }

    /**
     * @return A {@link VTDNav} that can be used to traverse or parse the XML
     *         manually or to create an {@link AutoPilot}.
     */
    public VTDNav getNavigator()
    {
        return this.navigator;
    }

    /**
     * Deserialize the text value of the current element of the {@link VTDNav
     * navigator} into the given type. The default value will be null.
     * 
     * @param clazz
     *            The type to deserialize to. Note! a {@link Mapper} has to be
     *            registered for this type
     * @return The deserialized type
     * @see SimpleVTD#registerMapper
     */
    public <T> T deserialize(final Class<T> clazz)
    {
        return deserialize(clazz, new String[0], null);
    }

    /**
     * Deserialize the text value of the current element of the {@link VTDNav
     * navigator} into the given type.
     * 
     * @param clazz
     *            The type to deserialize to. Note! a {@link Mapper} has to be
     *            registered for this type
     * @param defaultValue
     *            For built in mappers, the default value is returned in case of
     *            any error.
     * @return The deserialized type or the default value
     * @see SimpleVTD#registerMapper
     */
    public <T> T deserialize(final Class<T> clazz, final T defaultValue)
    {
        return deserialize(clazz, new String[0], defaultValue);
    }

    /**
     * Deserialize the given element (relative to the current {@link VTDNav
     * navigator} position) into the given type.
     * 
     * @param clazz
     *            The type to deserialize to. Note! a {@link Mapper} has to be
     *            registered for this type
     * @param elementName
     *            This can be either the name of a child element to traverse
     *            into, or a path (like a simple XPath expression) to traverse
     *            into.<br>
     *            If a path is provided, the elements should be separated with a
     *            forward slash "/". An attribute is denoted, the attribute name
     *            should be prefixed with a "@". If a path is provided, the
     *            {@link VTDNav navigator} will be rewinded to the previous
     *            position before returning.
     *            <p>
     *            <b>Example</b>:
     * 
     *            <pre>
     * {@code
     * <person gender="m">
     *    <name>John<name>
     *    <phone type="mobile">
     *       <number>12345</number>
     *    </phone>
     * <person>
     * }
     * </pre>
     * 
     *            If you have the {@link VTDNav navigator} positioned on the
     *            {@code<person>} element this is how to:
     *            <ul>
     *            <li>Query the "gender" attribute: <b>"@gender"</b></li>
     *            <li>Query the "name" element: <b>"name"</b></li>
     *            <li>Query the "type" attribute of the "phone" element:
     *            <b>"phone/@type"</b></li>
     *            <li>Query the "number" element of the "phone" element:
     *            <b>"phone/element"</b</li>
     *            </ul>
     *            </p>
     * @param defaultValue
     *            For built in mappers, the default value is returned in case of
     *            any error.
     * @return The deserialized type or the default value
     * @see SimpleVTD#registerMapper
     */
    public <T> T deserialize(final Class<T> clazz, final String elementName, final T defaultValue)
    {
        return deserialize(clazz, StringUtils.split(elementName, '/'), defaultValue);
    }

    private <T> T deserialize(final Class<T> clazz, final String[] elementNames, final T defaultValue)
    {
        try
        {
            if(elementNames.length == 0)
            {
                return doDeserialize(clazz, defaultValue);
            }
            else if(StringUtils.startsWith(elementNames[0], "@")) //Attribute
            {
                return deserializeAttribute(clazz, StringUtils.removeStart(elementNames[0], "@"), defaultValue);
            }
            else if(this.navigator.toElement(VTDNav.FIRST_CHILD, elementNames[0]))
            {
                try
                {
                    if(elementNames.length == 1)
                    {
                        return doDeserialize(clazz, defaultValue);
                    }
                    else
                    {
                        return deserialize(clazz, Arrays.copyOfRange(elementNames, 1, elementNames.length), defaultValue);
                    }
                }
                finally
                {
                    this.navigator.toElement(VTDNav.PARENT);
                }
            }
        }
        catch(final Exception e)
        {
            log.warn(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * Deserializes the given element (relative to the current {@link VTDNav
     * navigator} position), and all siblings into the given type, and adds them
     * to the collection.
     * 
     * @param clazz
     *            The type to deserialize to. Note! a {@link Mapper} has to be
     *            registered for this type
     * @param elementName
     *            This can be either the name of a child element to traverse
     *            into, or a path (like a simple XPath expression) to traverse
     *            into.<br>
     *            If a path is provided, the elements should be separated with a
     *            forward slash "/". Attributes are not supported. If a path is
     *            provided, the {@link VTDNav navigator} will be rewinded to the
     *            previous position before returning.
     *            <p>
     *            <b>Example</b>:
     * 
     *            <pre>
     * {@code
     * <person gender="m">
     *    <name>John<name>
     *    <phone type="mobile">
     *       <number>12345</number>
     *    </phone>
     *    <phone type="home">
     *       <number>54321</number>
     *    </phone>
     * <person>
     * }
     * </pre>
     * 
     *            With the {@link VTDNav navigator} positioned on the
     *            {@code<person>} and with a registered {@link Mapper mapper}
     *            for the type "Phone" the following code can be used to return
     *            a collection with all phones:<br>
     *            {@code Collection<Phone> phones =
     *            context.deserializeAll(Phone.class, "phone"); * }
     * @return A collection containing all deserialized elements, or an empty
     *         collection if no elements was found or if an error occurred.
     * @see SimpleVTD#registerMapper
     */
    public <T> Collection<T> deserializeAll(final Class<T> clazz, final String elementName)
    {
        return deserializeAll(clazz, StringUtils.split(elementName, '/'));
    }

    private <T> Collection<T> deserializeAll(final Class<T> clazz, final String[] elementNames)
    {
        final Collection<T> collection = new ArrayList<T>();
        try
        {
            if(this.navigator.toElement(VTDNav.FIRST_CHILD, elementNames[0]))
            {
                try
                {
                    do
                    {
                        if(elementNames.length == 1)
                        {
                            final T entity = doDeserialize(clazz, null);
                            if(entity != null)
                            {
                                collection.add(entity);
                            }
                        }
                        else
                        {
                            collection.addAll(deserializeAll(clazz, Arrays.copyOfRange(elementNames, 1, elementNames.length)));
                        }
                    } while(this.navigator.toElement(VTDNav.NEXT_SIBLING, elementNames[0]));
                }
                finally
                {
                    this.navigator.toElement(VTDNav.PARENT);
                }
            }
        }
        catch(final Exception e)
        {
            log.warn(e.getMessage(), e);
        }
        return collection;
    }

    @SuppressWarnings("unchecked")
    private <T> T deserializeAttribute(final Class<T> clazz, final String attribute, final T defaultValue) throws Exception
    {
        if(!this.mappers.containsKey(clazz))
        {
            throw new IllegalArgumentException("No mapper found for type: " + clazz);
        }
        final Mapper<T> mapper = (Mapper<T>)this.mappers.get(clazz);
        return mapper.deserializeAttribute(this, attribute, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private <T> T doDeserialize(final Class<T> clazz, final T defaultValue) throws Exception
    {
        if(this.mappers.containsKey(clazz))
        {
            final Mapper<T> mapper = (Mapper<T>)this.mappers.get(clazz);
            return mapper.deserialize(this, defaultValue);
        }
        else
        {
            throw new IllegalArgumentException("No mapper found for type: " + clazz);
        }
    }
}