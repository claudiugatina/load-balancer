import java.util.concurrent.Semaphore;

class Provider
{
    private static Integer providerOrdinal = 0;
    private static Integer defaultMaximumParallelRequests = 3;
    private String uniqueIdentifier;
    private boolean isAlive = true;

    public static Integer getMaximumParallelRequests() {
        return defaultMaximumParallelRequests;
    }

    public Provider()
    {
        uniqueIdentifier = "Provider_number_" + providerOrdinal;
        providerOrdinal++;
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