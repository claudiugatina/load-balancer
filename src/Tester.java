import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Tester {

    // Step 1 - Generate Provider
    private static void testProvider() {
        Set<String> uniqueIdentifiers = new HashSet<>();
        int numberOfProviders = 100;

        for (int i = 0; i < numberOfProviders; ++i) {
            Provider provider = new Provider();
            String uniqueIdentifier = provider.get();
            assert (!uniqueIdentifiers.contains(uniqueIdentifier));
            uniqueIdentifiers.add(provider.get());
        }
    }

    // Step 2 - Register a list of providers
    private static void testRegistering() {
        LoadBalancer loadBalancer = new LoadBalancer();

        int maxProvidersPerLoadBalancer = 10;
        Provider[] providers = new Provider[maxProvidersPerLoadBalancer];
        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i)
            providers[i] = new Provider();

        for (Provider provider : providers) {
            try {
                loadBalancer.registerProvider(provider);
            }
            catch (SizeLimitExceededException e) {
                assert (false);
            }
        }

        Provider extraProvider = new Provider();
        try {
            loadBalancer.registerProvider(extraProvider);
            assert (false);
        }
        catch (Exception e) {
            // we don't need to handle this exception, we are just checking that it is thrown
        }

    }

    public static void main(String[] args) {
        testProvider();
        testRegistering();
    }
}