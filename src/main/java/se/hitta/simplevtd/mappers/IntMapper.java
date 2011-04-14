package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.VTDNav;

public class IntMapper extends Mapper<Integer>
{
	@Override
	public Integer deserialize(SimpleVTDContext simpleVTDContext, Integer defaultValue) throws Exception
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getText();
			if (val != -1)
			{
				return Integer.parseInt(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public Integer deserializeAttribute(SimpleVTDContext simpleVTDContext, String attribute, Integer defaultValue)
	{
		VTDNav navigator = simpleVTDContext.getNavigator();

		try
		{
			int val = navigator.getAttrVal(attribute);

			if (val != -1)
			{
				return Integer.parseInt(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}
}
