package se.hitta.simplevtd;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.hitta.simplevtd.mappers.Mapper;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;

/**
 * Use SimpleVTDContext to deserialize XML into entities.<br>
 * A SimpleVTDContext is created using the factory method: {@link SimpleVTD#createContext}
 */
public final class SimpleVTDContext
{
    private static final Logger log = LoggerFactory.getLogger(SimpleVTDContext.class);
    private final Map<Type, Mapper<?>> mappers;
    private final VTDNav navigator;

    /**
     * User extras to store information that has to be passed with the context.
     */
    public final Map<String, Object> extras;

    SimpleVTDContext(final VTDNav navigator, final Map<Type, Mapper<?>> mappers, Map<String, Object> extras)
    {
        this.navigator = navigator;
        this.mappers = mappers;
        this.extras = extras;
    }
    
    /**
     * Clones the context (including mappers and extras) and the underlying VTDNav instance to get with shared XML, VTD and LC buffers The node position is also copied from the original instance.
     * 
     */
    @Override
    public SimpleVTDContext clone()
    {
        return new SimpleVTDContext(this.navigator.cloneNav(), this.mappers, this.extras);
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
        return deserialize(clazz, null, new String[0], null);
    }

    /**
     * Deserialize the text value of the current element of the {@link VTDNav
     * navigator} into the given type. The default value will be null.
     * 
     * @param mapper
     *            The {@link Mapper} to use for the deserialization
     * @return The deserialized type
     */
    public <T> T deserialize(final Mapper<T> mapper)
    {
        return deserialize(null, mapper, new String[0], null);
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
        return deserialize(clazz, null, new String[0], defaultValue);
    }

    /**
     * Deserialize the text value of the current element of the {@link VTDNav
     * navigator} into the given type.
     * 
     * @param mapper
     * @param defaultValue
     *            The default value is returned in case of
     *            any error. This is also the value sent to the {@link Mapper}.
     *            The {@link Mapper} to use for the deserialization
     * @return The deserialized type or the default value (depending on the {@link Mapper} implementation)
     * @see SimpleVTD#registerMapper
     */
    public <T> T deserialize(final Mapper<T> mapper, final T defaultValue)
    {
        return deserialize(null, mapper, new String[0], defaultValue);
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
     *            should be prefixed with a "@". If a path is provided, the {@link VTDNav navigator} will be rewinded to the previous
     *            position before returning.
     *            <p>
     *            <b>Example</b>:
     * 
     *            <pre> {@code
     * <person gender="m">
     *    <name>John<name>
     *    <phone type="mobile">
     *       <areaCode>08</areaCode>
     *       <number>12345</number>
     *    </phone>
     *    <phone type="fixed">
     *       <areaCode>031</areaCode>
     *       <number>12345</number>
     *    </phone>
     *    <phone type="fixed">
     *       <areaCode>08</areaCode>
     *       <number>22345</number>
     *    </phone>
     * <person>
     * } </pre>
     * 
     *            If you have the {@link VTDNav navigator} positioned on the {@code<person>} element this is how to:
     *            <ul>
     *            <li>Query the "gender" attribute: <b>"@gender"</b></li>
     *            <li>Query the "name" element: <b>"name"</b></li>
     *            <li>Query the "type" attribute of the first "phone" element:
     *            <b>"phone/@type"</b></li>
     *            <li>Query the "number" element of the first "phone" element:
     *            <b>"phone/element"</b></li>
     *            </ul>
     *            </p>
     *            <p>
     *            <b>Guards</b></br>
     *            Guards can be used to ensure serialization of a certain element. A guard is expressed as: [{@code<node name>}=value]
     *            where {@code<node name>} can be either an attribute (prefixed with '@') or the name of an element. </br>
     *            If you have the {@link VTDNav navigator} positioned on the {@code<person>} element this is how to:
     *            <ul>
     *            <li>Query the first "fixed" phone:
     *            <b>"phone[@type=fixed]"</b></li>
     *            <li>Query the "number" element of the first "fixed" phone:
     *            <b>"phone[@type=fixed]/number"</b></li>
     *            <li>Query the "number" element of the first phone with areaCode=031:
     *            <b>"phone[areaCode=031]/number"</b></li>
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
        return deserialize(clazz, null, StringUtils.split(elementName, '/'), defaultValue);
    }

    /**
     * Deserialize the given element (relative to the current {@link VTDNav
     * navigator} position) into the given type.
     * 
     * @param mapper
     *            The {@link Mapper} to use for the deserialization
     * @param elementName
     *            For information about the elementName parameter see: {@link #deserialize(Class, String, Object) deserialize}
     * @param defaultValue
     *            The default value is returned in case of
     *            any error. This is also the value sent to the {@link Mapper}.
     * @return The deserialized type or the default value
     */
    public <T> T deserialize(final Mapper<T> mapper, final String elementName, final T defaultValue)
    {
        return deserialize(null, mapper, StringUtils.split(elementName, '/'), defaultValue);
    }

    private static final String GUARD_START_CHAR = "[";

    private <T> T deserialize(final Class<T> clazz, final Mapper<T> mapper, final String[] elementNames, final T defaultValue)
    {
        try
        {
            if(elementNames.length == 0)
            {
                return doDeserialize(clazz, mapper, defaultValue);
            }
            else if(StringUtils.startsWith(elementNames[0], "@")) //Attribute
            {
                return deserializeAttribute(clazz, mapper, StringUtils.removeStart(elementNames[0], "@"), defaultValue);
            }

            final GuardedElement guardedElement = getGuard(elementNames[0]);

            if(this.navigator.toElement(VTDNav.FIRST_CHILD, guardedElement.getElementName(elementNames[0])))
            {
                try
                {
                    if(!checkGuard(guardedElement))
                    {
                        return defaultValue;
                    }
                    if(elementNames.length == 1)
                    {
                        return doDeserialize(clazz, mapper, defaultValue);
                    }
                    return deserialize(clazz, mapper, Arrays.copyOfRange(elementNames, 1, elementNames.length), defaultValue);

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
     *            <pre> {@code
     * <person gender="m">
     *    <name>John<name>
     *    <phone type="mobile">
     *       <areaCode>08</areaCode>
     *       <number>12345</number>
     *    </phone>
     *    <phone type="fixed">
     *       <areaCode>031</areaCode>
     *       <number>12345</number>
     *    </phone>
     *    <phone type="fixed">
     *       <areaCode>08</areaCode>
     *       <number>22345</number>
     *    </phone>
     * <person>
     * } </pre>
     * 
     *            With the {@link VTDNav navigator} positioned on the {@code<person>} and with a registered {@link Mapper mapper} for the type "Phone" the
     *            following code can be used to return
     *            a collection with all phones:</br>
     *            <b>{@code Collection<Phone> phones = context.deserializeAll(Phone.class, "phone");}</b>
     *            <p>
     *            <b>Guards</b></br>
     *            Guards can be used to ensure serialization of a certain element. A guard is expressed as: [{@code<node name>}=value]
     *            where {@code<node name>} can be either an attribute (prefixed with '@') or the name of an element. </br>
     *            If you have the {@link VTDNav navigator} positioned on the {@code<person>} element this is how to:
     *            <ul>
     *            <li>Retrieve all "fixed" phones:
     *            <b>{@code context.deserializeAll(Phone.class, "phone[@type=fixed]");}</b></li>
     *            <li>Retrieve all phones with areaCode=08:
     *            <b>{@code context.deserializeAll(Phone.class, "phone[areaCode=08]");}</b></li>
     *            <li>Retrieve all numbers where the areaCode=08:
     *            <b>{@code context.deserializeAll(String.class, "phone[areaCode=08]/number");}</b></li>
     *            </ul>
     *            </p>
     * @return A collection containing all deserialized elements, or an empty
     *         collection if no elements was found or if an error occurred.
     * @see SimpleVTD#registerMapper
     */
    public <T> Collection<T> deserializeAll(final Class<T> clazz, final String elementName)
    {
        return deserializeAll(clazz, null, StringUtils.split(elementName, '/'));
    }

    /**
     * Deserialize the given element (relative to the current {@link VTDNav
     * navigator} position) into the given type.
     * 
     * @param mapper
     *            The {@link Mapper} to use for the deserialization
     * @param elementName
     *            For information about the elementName parameter see: {@link #deserializeAll(Class, String) deserializeAll}
     * @return A collection containing all deserialized elements, or an empty
     *         collection if no elements was found or if an error occurred.
     */
    public <T> Collection<T> deserializeAll(final Mapper<T> mapper, final String elementName)
    {
        return deserializeAll(null, mapper, StringUtils.split(elementName, '/'));
    }

    private <T> Collection<T> deserializeAll(final Class<T> clazz, final Mapper<T> mapper, final String[] elementNames)
    {
        final Collection<T> collection = new ArrayList<T>();
        try
        {
            final GuardedElement guardedElement = getGuard(elementNames[0]);
            final String elementName = guardedElement.getElementName(elementNames[0]);

            if(this.navigator.toElement(VTDNav.FIRST_CHILD, elementName))
            {
                try
                {
                    do
                    {
                        if(!checkGuard(guardedElement))
                        {
                            return collection;
                        }

                        if(elementNames.length == 1)
                        {
                            final T entity = doDeserialize(clazz, mapper, null);
                            if(entity != null)
                            {
                                collection.add(entity);
                            }
                        }
                        else
                        {
                            collection.addAll(deserializeAll(clazz, mapper, Arrays.copyOfRange(elementNames, 1, elementNames.length)));
                        }
                    } while(this.navigator.toElement(VTDNav.NEXT_SIBLING, elementName));
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

    private boolean checkGuard(GuardedElement guardedElement) throws Exception
    {
        if(guardedElement.isGuarded)
        {
            boolean guardOk = false;
            do
            {
                if(StringUtils.startsWith(guardedElement.guardElement, "@")) //Attribute
                {
                    String guardValue = deserializeAttribute(String.class, null, StringUtils.removeStart(guardedElement.guardElement, "@"), null);

                    if((guardOk = guardedElement.expected.equals(guardValue)) == true)
                    {
                        break;
                    }
                }
                else if(this.navigator.toElement(VTDNav.FIRST_CHILD, guardedElement.guardElement))
                {
                    try
                    {
                        do
                        {
                            String guardValue = doDeserialize(String.class, null, null);

                            if((guardOk = guardedElement.expected.equals(guardValue)) == true)
                            {
                                break;
                            }

                        } while(this.navigator.toElement(VTDNav.NEXT_SIBLING, guardedElement.guardElement));
                        if(guardOk)
                            break;
                    }
                    finally
                    {
                        this.navigator.toElement(VTDNav.PARENT);
                    }
                }

            } while(this.navigator.toElement(VTDNav.NEXT_SIBLING, guardedElement.element));

            return guardOk;
        }

        return true;
    }

    private static class GuardedElement
    {
        final String element;
        final boolean isGuarded;
        final String guardElement;
        final String expected;

        static GuardedElement noGuard = new GuardedElement(false, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        GuardedElement(boolean isGuarded, String element, String guardElement, String expected)
        {
            this.isGuarded = isGuarded;
            this.element = element;
            this.guardElement = guardElement;
            this.expected = expected;
        }

        String getElementName(String elementName)
        {
            return this.isGuarded ? this.element : elementName;
        }
    }

    private static GuardedElement getGuard(final String guardedElement)
    {
        int guardStart = guardedElement.indexOf(GUARD_START_CHAR);

        if(guardStart > -1)
        {
            //element has guard!
            final String element = guardedElement.substring(0, guardStart);
            final String guard = guardedElement.substring(guardStart + 1, guardedElement.length() - 1);
            final String parts[] = guard.split("=");

            final String guardElement = parts[0];
            final String expected = parts[1];

            return new GuardedElement(true, element, guardElement, expected);
        }

        return GuardedElement.noGuard;
    }

    @SuppressWarnings("unchecked")
    private <T> T deserializeAttribute(final Class<T> clazz, final Mapper<T> mapper, final String attribute, final T defaultValue) throws Exception
    {
        if(mapper != null)
        {
            return mapper.deserializeAttribute(this, attribute, defaultValue);
        }

        if(!this.mappers.containsKey(clazz))
        {
            throw new IllegalArgumentException("No mapper found for type: " + clazz);
        }

        final Mapper<T> registeredMapper = (Mapper<T>)this.mappers.get(clazz);
        return registeredMapper.deserializeAttribute(this, attribute, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private <T> T doDeserialize(final Class<T> clazz, final Mapper<T> mapper, final T defaultValue) throws Exception
    {
        if(mapper != null)
        {
            return mapper.deserialize(this, defaultValue);
        }

        if(!this.mappers.containsKey(clazz))
        {
            throw new IllegalArgumentException("No mapper found for type: " + clazz);
        }

        final Mapper<T> registeredMapper = (Mapper<T>)this.mappers.get(clazz);
        return registeredMapper.deserialize(this, defaultValue);

    }
}