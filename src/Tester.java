import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import java.util.*;

public class Tester {
    private static int maxProvidersPerLoadBalancer = 10;

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

    // Step 3 - random invocation
    // An idea would be to compute the standard deviation of the invocation histogram and see how likely
    // it is that the numbers are really random, but every once in a while the test might fail.
    // I think checking this by eye is enough for the requirements of the problem.
    private static void testRandomLoadBalancing() {
        LoadBalancer loadBalancer = buildLoadBalancerWithProviders(Protocol.RANDOM);
        int testCases = maxProvidersPerLoadBalancer * 2;
        for (int i = 0; i < testCases; ++i) {
            System.out.println(loadBalancer.get());
        }
    }

    // Step 4 - Round Robin invocation
    private static void testRoundRobinLoadBalancing() {
        LoadBalancer loadBalancer = buildLoadBalancerWithProviders(Protocol.ROUND_ROBIN);
        String[] uniqueIdentifiers = new String[maxProvidersPerLoadBalancer];

        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i)
            uniqueIdentifiers[i] = loadBalancer.get();

        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i)
            assert (uniqueIdentifiers[i].equals(loadBalancer.get()));
    }

    // Step 5 - manual node exclusion/inclusion
    private static void testIncludeExclude() {
        Provider provider1 = new Provider();
        Provider provider2 = new Provider();

        LoadBalancer loadBalancer = new LoadBalancer(Protocol.ROUND_ROBIN);

        try {
            loadBalancer.registerProvider(provider1);
            loadBalancer.registerProvider(provider2);
        }
        catch (Exception e) {
            assert (false);
        }

        assert (!loadBalancer.get().equals(loadBalancer.get()));

        loadBalancer.excludeProvider(provider1);

        assert (loadBalancer.get().equals(provider2.get()));
        assert (loadBalancer.get().equals(provider2.get()));
    }

    // Step 6 & 7 - heart beat checker
    private static void testHeartbeatChecker() throws InterruptedException {
        LoadBalancer loadBalancer = new LoadBalancer(Protocol.ROUND_ROBIN);
        Provider[] providers = generateProviders();
        loadBalancer.registerProviders(providers);
        providers[3].kill();
        Thread.sleep(7000);

        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i) {
            assert (!loadBalancer.get().equals(providers[3].get()));
        }

        providers[3].fix();
        Thread.sleep(14000);

        boolean backInBusiness = false;
        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i) {
            if (loadBalancer.get().equals(providers[3].get()))
                backInBusiness = true;
        }

        assert (backInBusiness);
    }

    private static LoadBalancer buildLoadBalancerWithProviders(Protocol protocol) {
        LoadBalancer loadBalancer = new LoadBalancer(protocol);
        Provider[] providers = generateProviders();
            loadBalancer.registerProviders(providers);
        return loadBalancer;
    }

    private static Provider[] generateProviders() {
        Provider[] providers = new Provider[maxProvidersPerLoadBalancer];
        for (int i = 0; i < maxProvidersPerLoadBalancer; ++i)
            providers[i] = new Provider();
        return providers;
    }

    public static void main(String[] args)  throws Exception{
        testProvider();
        testRegistering();
        testRandomLoadBalancing();
        testRoundRobinLoadBalancing();
        testIncludeExclude();
        testHeartbeatChecker();
    }
}