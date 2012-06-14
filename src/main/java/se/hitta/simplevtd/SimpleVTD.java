package se.hitta.simplevtd;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import se.hitta.simplevtd.mappers.BooleanMapper;
import se.hitta.simplevtd.mappers.DoubleMapper;
import se.hitta.simplevtd.mappers.IntMapper;
import se.hitta.simplevtd.mappers.LongMapper;
import se.hitta.simplevtd.mappers.Mapper;
import se.hitta.simplevtd.mappers.StringMapper;

import com.ximpleware.VTDGen;

/**
 * A thread safe context factory and container for mappers. Default mappers for the
 * following java types are included:
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link Integer}</li>
 * <li>{@link Long}</li>
 * <li>{@link Double}</li>
 * <li>{@link Boolean}</li>
 * </ul>
 * Register your own custom mappers using {@link #registerMapper}.
 * 
 */
public final class SimpleVTD
{
    private final Map<Type, Mapper<?>> mappers = new HashMap<Type, Mapper<?>>();

    /**
     * Creates a new thread safe SimpleVTD.
     */
    public SimpleVTD()
    {
        this.mappers.put(String.class, new StringMapper());
        this.mappers.put(Integer.class, new IntMapper());
        this.mappers.put(Long.class, new LongMapper());
        this.mappers.put(Double.class, new DoubleMapper());
        this.mappers.put(Boolean.class, new BooleanMapper());
    }

    /**
     * Register your own custom {@link Mapper mapper}. Mappers have to be
     * registered before a context that is dependent on them is created.
     * 
     * @param <T>
     *            A {@link Mapper mapper} can only be registered for the type
     *            the {@link Mapper mapper} handles
     * @param clazz
     *            The type to register the {@link Mapper mapper} for
     * @param mapper
     *            The {@link Mapper mapper} to register
     */
    public synchronized <T> SimpleVTD registerMapper(final Class<T> clazz, final Mapper<T> mapper)
    {
        this.mappers.put(clazz, mapper);
        return this;
    }

    /**
     * Create a mapping context. Note! the mapping context is not thread safe.
     * 
     * @param xml
     *            The XML to process as a byte array
     * @return A new {@link SimpleVTDContext}
     * @throws Exception
     *             in case of XML parse exceptions
     */
    public SimpleVTDContext createContext(final byte[] xml) throws Exception
    {
        final VTDGen vtdGen = new VTDGen();
        vtdGen.setDoc(xml);
        vtdGen.parse(false);
        return new SimpleVTDContext(vtdGen.getNav(), this.mappers, new HashMap<String, Object>());
    }
}
