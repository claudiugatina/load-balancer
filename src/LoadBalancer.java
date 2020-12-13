import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer
{
    private static Integer maximumNumberOfProviders = 10;
    private List<Provider> providers = new ArrayList<>();

    public void registerProvider(Provider provider) throws SizeLimitExceededException {
        if (providers.size() >= maximumNumberOfProviders)
            throw new SizeLimitExceededException();
        else
            providers.add(provider);
    }

}