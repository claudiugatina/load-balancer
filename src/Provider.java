import java.util.Set;

class Provider
{
    private static Integer numberOfProviders = 0;
    private String uniqueIdentifier;

    public Provider()
    {
        uniqueIdentifier = "Provider_number_" + numberOfProviders;
        numberOfProviders++;
    }

    public String get()
    {
        return uniqueIdentifier;
    }
}