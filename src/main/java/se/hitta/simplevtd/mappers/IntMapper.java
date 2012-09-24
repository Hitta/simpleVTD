package se.hitta.simplevtd.mappers;

import org.apache.commons.lang3.StringUtils;

public class IntMapper extends Mapper<Integer>
{
    @Override
    public Integer parse(final String raw, Integer defaultValue)
    {
        if(StringUtils.isBlank(raw)) return defaultValue;

        return Integer.parseInt(raw);
    }
}
