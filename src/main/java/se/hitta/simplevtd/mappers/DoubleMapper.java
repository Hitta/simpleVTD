package se.hitta.simplevtd.mappers;

public class DoubleMapper extends Mapper<Double>
{
    @Override
    public Double parse(final String raw)
    {
        return Double.parseDouble(raw);
    }
}
