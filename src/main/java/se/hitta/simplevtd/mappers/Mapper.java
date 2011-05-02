package se.hitta.simplevtd.mappers;

import se.hitta.simplevtd.SimpleVTDContext;

import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

public abstract class Mapper<T>
{
    @SuppressWarnings("unchecked")
    public T parse(final String raw)
    {
        return (T)raw;
    }

    public T deserialize(final SimpleVTDContext context, final T defaultValue)
    {
        try
        {
            final VTDNav navigator = context.getNavigator();
            final int val = navigator.getText();
            final String raw = (val == -1) ? null : navigator.toNormalizedString(val);
            return (raw == null) ? defaultValue : parse(raw);
        }
        catch(final NavException e)
        {
            return defaultValue;
        }
    }

    public T deserializeAttribute(final SimpleVTDContext context, final String attribute, final T defaultValue)
    {
        try
        {
            final VTDNav navigator = context.getNavigator();
            final int val = navigator.getAttrVal(attribute);
            final String raw = (val == -1) ? null : navigator.toNormalizedString(val);
            return (raw == null) ? defaultValue : parse(raw);
        }
        catch(final NavException e)
        {
            return defaultValue;
        }
    }
}