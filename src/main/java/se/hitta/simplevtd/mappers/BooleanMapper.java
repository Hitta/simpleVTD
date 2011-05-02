package se.hitta.simplevtd.mappers;

public class BooleanMapper extends Mapper<Boolean>
{
    @Override
    public Boolean parse(final String raw)
    {
        return Boolean.parseBoolean(raw);
    }
}