public class SlowProvider extends Provider {

    // this class is only used to test step 8
    @Override
    public String get() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {}
        return super.get();
    }
}
