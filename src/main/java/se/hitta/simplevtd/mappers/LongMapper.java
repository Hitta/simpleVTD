package se.hitta.simplevtd.mappers;

public class LongMapper extends Mapper<Long>
{
    @Override
    public Long parse(final String raw)
    {
        return Long.parseLong(raw);
    }
}
