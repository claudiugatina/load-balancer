import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadBalancer
{
    private static Integer maximumNumberOfProviders = 10;
    private List<Provider> providers = new ArrayList<>();
    private Integer nextRoundRobinIndex = 0;
    private Protocol protocol;

    public synchronized void registerProvider(Provider provider) throws SizeLimitExceededException {
        if (providers.size() >= maximumNumberOfProviders)
            throw new SizeLimitExceededException();
        else
            providers.add(provider);
    }

    private String getRandom() {
        Random randomNumberGenerator = new Random();
        int idx = randomNumberGenerator.nextInt(providers.size());
        Provider provider = providers.get(idx);
        return provider.get();
    }

    private String getRoundRobin() {
        Provider provider = providers.get(nextRoundRobinIndex);
        nextRoundRobinIndex++;
        nextRoundRobinIndex %= providers.size();
        return provider.get();
    }

    public synchronized String get() {
        switch (protocol) {
            case RANDOM:
                return getRandom();
            case ROUND_ROBIN:
                return getRoundRobin();
            default:
                return getRandom();
        }
    }

    public LoadBalancer() {
        protocol = Protocol.RANDOM;
    }

    public LoadBalancer(Protocol protocol) {
        this.protocol = protocol;
    }
}