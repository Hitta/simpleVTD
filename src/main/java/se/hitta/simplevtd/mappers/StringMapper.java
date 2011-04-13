package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.VTDNav;

public class StringMapper extends Mapper<String>
{
	@Override
	public String deserialize(SimpleVTDContext muppetContext, String defaultValue) throws Exception
	{
		VTDNav navigator = muppetContext.getNavigator();

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
	public String deserializeAttribute(SimpleVTDContext muppetContext, String attribute, String defaultValue)
	{
		VTDNav navigator = muppetContext.getNavigator();

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
