package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.VTDNav;

public class StringMapper extends Mapper<String>
{
	@Override
	public String deserialize(SimpleVTDContext simpleVTDContext, String defaultValue) throws Exception
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getText();
			if (val != -1)
			{
				return navigator.toNormalizedString(val);
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public String deserializeAttribute(SimpleVTDContext simpleVTDContext, String attribute, String defaultValue)
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getAttrVal(attribute);

			if (val != -1)
			{
				return navigator.toNormalizedString(val);
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}
}
