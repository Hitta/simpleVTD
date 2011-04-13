package se.hitta.simplevtd;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import se.hitta.simplevtd.mappers.Mapper;

import com.ximpleware.VTDNav;

public class SimpleVTDContext
{
	private final Map<Class<?>, Mapper<?>> mappers;
	private final VTDNav navigator;

	public SimpleVTDContext(VTDNav navigator, Map<Class<?>, Mapper<?>> mappers)
	{
		this.navigator = navigator;
		this.mappers = mappers;
	}
	
	public VTDNav getNavigator()
	{
		return navigator;
	}
	
	public<T> T deserialize(Class<T> clazz)
	{
		return deserialize(clazz, new String[]{}, null);
	}
	
	public<T> T deserialize(Class<T> clazz, T defaultValue)
	{
		return deserialize(clazz, new String[]{}, defaultValue);
	}
	
	public<T> T deserialize(Class<T> clazz, String elementName, T defaultValue)
	{
		return deserialize(clazz, StringUtils.split(elementName, '/'), defaultValue);
	}
	
	private<T> T deserialize(Class<T> clazz, String[] elementNames, T defaultValue)
	{
		try
		{
			if(elementNames.length == 0)
			{
				return doDeserialize(clazz, defaultValue);
			}
			
			if(StringUtils.startsWith(elementNames[0], "@")) //Attribute
			{
				return deserializeAttribute(clazz, StringUtils.removeStart(elementNames[0], "@"), defaultValue);
			}
			
			if(navigator.toElement(VTDNav.FIRST_CHILD, elementNames[0]))
			{
				try
				{
					if(elementNames.length == 1)
					{
						return doDeserialize(clazz, defaultValue);
					}
					else
					{
						//traverse
						return deserialize(clazz, Arrays.copyOfRange(elementNames, 1, elementNames.length), defaultValue);
					}
					
				}
				finally
				{
					navigator.toElement(VTDNav.PARENT);				
				}
			}
		}catch(Exception e){}
		
		return defaultValue;
	}
	
	@SuppressWarnings("unchecked")
	private<T> T deserializeAttribute(Class<T> clazz, String attribute, T defaultValue) throws Exception
	{
		if(!mappers.containsKey(clazz))
		{
			throw new IllegalArgumentException("No mapper found for type: " + clazz);
		}
		
		Mapper<T> mapper = (Mapper<T>)mappers.get(clazz);
		
		return mapper.deserializeAttribute(this, attribute, defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	private<T> T doDeserialize(Class<T> clazz, T defaultValue) throws Exception
	{
		if(!mappers.containsKey(clazz))
		{
			throw new IllegalArgumentException("No mapper found for type: " + clazz);
		}
		
		Mapper<T> mapper = (Mapper<T>)mappers.get(clazz);
		
		return mapper.deserialize(this, defaultValue);
	}
}
