import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadBalancer
{
    private static Integer maximumNumberOfProviders = 10;
    private List<Provider> providers = new ArrayList<>();

    public synchronized void registerProvider(Provider provider) throws SizeLimitExceededException {
        if (providers.size() >= maximumNumberOfProviders)
            throw new SizeLimitExceededException();
        else
            providers.add(provider);
    }

    public synchronized String get() {
        Random randomNumberGenerator = new Random();
        int idx = randomNumberGenerator.nextInt(providers.size());
        Provider provider = providers.get(idx);
        return provider.get();
    }

}