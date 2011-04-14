package se.hitta.simplevtd.mappers;

import com.ximpleware.VTDNav;

import se.hitta.simplevtd.SimpleVTDContext;

public class BooleanMapper extends Mapper<Boolean>
{

	@Override
	public Boolean deserialize(SimpleVTDContext simpleVTDContext, Boolean defaultValue) throws Exception
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getText();
			if (val != -1)
			{
				return Boolean.parseBoolean(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}

	@Override
	public Boolean deserializeAttribute(SimpleVTDContext simpleVTDContext, String attribute, Boolean defaultValue)
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getAttrVal(attribute);

			if (val != -1)
			{
				return Boolean.parseBoolean(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}
}
