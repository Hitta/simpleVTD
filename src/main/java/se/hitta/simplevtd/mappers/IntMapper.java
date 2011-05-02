package se.hitta.simplevtd.mappers;

public class IntMapper extends Mapper<Integer>
{
    @Override
    public Integer parse(final String raw)
    {
        return Integer.parseInt(raw);
    }
}
