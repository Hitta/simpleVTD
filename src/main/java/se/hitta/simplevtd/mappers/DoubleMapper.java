package se.hitta.simplevtd.mappers;

import org.apache.commons.lang3.StringUtils;

public class DoubleMapper extends Mapper<Double>
{
    @Override
    public Double parse(final String raw, Double defaultValue)
    {
        if(StringUtils.isBlank(raw)) return defaultValue;

        return Double.parseDouble(raw);
    }
}
