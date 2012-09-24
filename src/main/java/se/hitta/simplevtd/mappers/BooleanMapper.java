package se.hitta.simplevtd.mappers;

import org.apache.commons.lang3.StringUtils;

public class BooleanMapper extends Mapper<Boolean>
{
    @Override
    public Boolean parse(final String raw, Boolean defaultValue)
    {
        if(StringUtils.isBlank(raw)) return defaultValue;

        return Boolean.parseBoolean(raw);
    }
}