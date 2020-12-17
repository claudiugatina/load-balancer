import java.util.*;
import java.util.concurrent.Semaphore;

public class HeartbeatChecker extends  Thread {
    private List<Provider> providers;
    private Set<Provider> aliveProviders = new HashSet<>();
    private Set<Provider> pendingProviders = new HashSet<>();
    private long heartbeatPeriod = 2000;

    public Boolean getAvailability(Provider provider) {
        return aliveProviders.contains(provider);
    }

    public void run() {
        while (true) {
            for (Provider provider : providers) {
                if(provider.check()) {
                    successfulCheck(provider);
                }
                else {
                    unsuccessfulCheck(provider);
                }
            }
            try {
                Thread.sleep(heartbeatPeriod);
            }
            catch (InterruptedException e) {}
        }
    }

    public Integer getNumberOfAliveProviders() {
        return aliveProviders.size();
    }

    private void successfulCheck(Provider provider) {
        if (aliveProviders.contains(provider)) {
        }
        else if (pendingProviders.contains(provider)) {
            pendingProviders.remove(provider);
            aliveProviders.add(provider);
        }
        else {
            pendingProviders.add(provider);
        }
    }

    private void unsuccessfulCheck(Provider provider) {
        pendingProviders.remove(provider);
        aliveProviders.remove(provider);
    }

    public void initialCheck(Provider provider) {
        if (provider.check())
            aliveProviders.add(provider);
    }

    public void setHeartbeatPeriod(long heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    public HeartbeatChecker(List<Provider> providers) {
        this.providers = providers;
    }
}
