public class Tester {

    private static void testProvider() {
        for (int i = 0; i < 10; ++i) {
            Provider provider = new Provider();
            System.out.println(provider.get());
        }
    }

    public static void main(String[] args) {
        testProvider();
    }
}