package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.VTDNav;

public class LongMapper extends Mapper<Long>
{
	@Override
	public Long deserialize(SimpleVTDContext muppetContext, Long defaultValue) throws Exception
	{
		VTDNav navigator = muppetContext.getNavigator();

		try
		{
			int val = navigator.getText();
			if (val != -1)
			{
				return Long.parseLong(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public Long deserializeAttribute(SimpleVTDContext muppetContext, String attribute, Long defaultValue)
	{
		VTDNav navigator = muppetContext.getNavigator();

		try
		{
			int val = navigator.getAttrVal(attribute);

			if (val != -1)
			{
				return Long.parseLong(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}
}
