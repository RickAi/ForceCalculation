public class Singleton {

    private Singleton() {}

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static final Singleton INSTANCE = new Singleton();
    }

}
