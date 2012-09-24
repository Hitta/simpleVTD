package se.hitta.simplevtd.mappers;

import org.apache.commons.lang3.StringUtils;

public class LongMapper extends Mapper<Long>
{
    @Override
    public Long parse(final String raw, Long defaultValue)
    {
        if(StringUtils.isBlank(raw)) return defaultValue;
        
        return Long.parseLong(raw);
    }
}
