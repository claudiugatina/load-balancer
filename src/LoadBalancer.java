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
    private HeartbeatChecker heartbeatChecker = new HeartbeatChecker(providers);

    private Provider getRandom() {
        Random randomNumberGenerator = new Random();
        int idx = randomNumberGenerator.nextInt(providers.size());
        return providers.get(idx);
    }

    private synchronized Provider getRoundRobin() {
        Provider provider = providers.get(nextRoundRobinIndex);
        nextRoundRobinIndex++;
        nextRoundRobinIndex %= providers.size();
        return provider;
    }

    public String get() {
        Provider provider;
        do {
            switch (protocol) {
                case RANDOM:
                    provider = getRandom();
                    break;
                case ROUND_ROBIN:
                    provider = getRoundRobin();
                    break;
                default:
                    provider = getRandom();
                    break;
            }
        } while (!heartbeatChecker.getAvailability(provider));
        return provider.get();
    }

    public synchronized void registerProvider(Provider provider) throws SizeLimitExceededException {
        if (providers.size() >= maximumNumberOfProviders) {
            throw new SizeLimitExceededException();
        }
        else {
            providers.add(provider);
            heartbeatChecker.initialCheck(provider);
        }
    }

    public void registerProviders(Provider[] providers) {
        for (Provider provider : providers) {
            try {
                registerProvider(provider);
            }
            catch (SizeLimitExceededException e) {
                System.out.println("Providers not registered");
            }
        }
    }

    public synchronized void excludeProvider(Provider provider) {
        providers.remove(provider);
    }

    public LoadBalancer() {
        protocol = Protocol.RANDOM;
        heartbeatChecker.start();
    }

    public LoadBalancer(Protocol protocol) {
        this.protocol = protocol;
        heartbeatChecker.start();
    }
}