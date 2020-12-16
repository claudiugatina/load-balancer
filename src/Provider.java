import java.util.Set;

class Provider
{
    private static Integer numberOfProviders = 0;
    private String uniqueIdentifier;
    private boolean isAlive = true;

    public Provider()
    {
        uniqueIdentifier = "Provider_number_" + numberOfProviders;
        numberOfProviders++;
    }

    public boolean check() {
        return isAlive;
    }

    // Not needed for the task, but needed for testing
    public void kill() {
        isAlive = false;
    }

    // Not needed for the task, but needed for testing
    public void fix() {
        isAlive = true;
    }

    public String get()
    {
        return uniqueIdentifier;
    }
}