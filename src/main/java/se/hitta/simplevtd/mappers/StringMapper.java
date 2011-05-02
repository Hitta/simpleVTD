package se.hitta.simplevtd.mappers;

public final class StringMapper extends Mapper<String>
{
    @Override
    public String parse(final String raw)
    {
        return raw;
    }
}
