package se.hitta.simplevtd.mappers;


import org.apache.commons.lang.NotImplementedException;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.AutoPilot;


public abstract class Mapper<T>
{
	public abstract T deserialize(SimpleVTDContext simpleVTDContext, T defaultValue) throws Exception;
	
	public T deserializeAttribute(SimpleVTDContext simpleVTDContext, String attribute, T defaultValue)
	{
		throw new NotImplementedException();
	}
	
	protected boolean navigateTo(AutoPilot autoPilot, String xPath)
	{
		try
		{
			autoPilot.selectXPath(xPath);
			return autoPilot.evalXPath() != -1;
		}
		catch (Exception e)
		{}
		return false;
	}
}
