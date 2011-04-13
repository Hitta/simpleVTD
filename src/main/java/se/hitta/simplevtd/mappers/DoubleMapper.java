package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.VTDNav;

public class DoubleMapper extends Mapper<Double>
{

	@Override
	public Double deserialize(SimpleVTDContext muppetContext, Double defaultValue) throws Exception
	{
		VTDNav navigator = muppetContext.getNavigator();

		try
		{
			int val = navigator.getText();
			if (val != -1)
			{
				return Double.parseDouble(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public Double deserializeAttribute(SimpleVTDContext muppetContext, String attribute, Double defaultValue)
	{
		VTDNav navigator = muppetContext.getNavigator();

		try
		{
			int val = navigator.getAttrVal(attribute);

			if (val != -1)
			{
				return Double.parseDouble(navigator.toNormalizedString(val));
			}
		}
		catch (Exception e)
		{
		}

		return defaultValue;
	}
}
