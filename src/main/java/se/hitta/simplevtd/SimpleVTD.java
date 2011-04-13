package se.hitta.simplevtd;

import java.util.HashMap;
import java.util.Map;

import se.hitta.simplevtd.mappers.BooleanMapper;
import se.hitta.simplevtd.mappers.DoubleMapper;
import se.hitta.simplevtd.mappers.IntMapper;
import se.hitta.simplevtd.mappers.LongMapper;
import se.hitta.simplevtd.mappers.Mapper;
import se.hitta.simplevtd.mappers.StringMapper;

import com.ximpleware.VTDGen;

public class SimpleVTD
{
	private final Map<Class<?>, Mapper<?>> mappers = new HashMap<Class<?>, Mapper<?>>();
	
	public SimpleVTD()
	{
		mappers.put(String.class, new StringMapper());
		mappers.put(Integer.class, new IntMapper());
		mappers.put(Double.class, new DoubleMapper());
		mappers.put(Boolean.class, new BooleanMapper());
		mappers.put(Long.class, new LongMapper());
	}
	
	public<T> SimpleVTD registerMapper(Class<T> clazz, Mapper<T> mapper)
	{
		mappers.put(clazz, mapper);
		
		return this;
	}
	
	public SimpleVTDContext createContext(byte[] xml) throws Exception
	{
		final VTDGen vtdGen = new VTDGen();
		vtdGen.setDoc(xml);
		vtdGen.parse(false);
		
		return new SimpleVTDContext(vtdGen.getNav(), mappers);
	}
}
