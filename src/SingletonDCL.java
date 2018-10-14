public class SingletonDCL {

    private static volatile SingletonDCL INSTANCE;

    private SingletonDCL() {}

    public static SingletonDCL getInstance() {
        if (INSTANCE == null) {
            synchronized (SingletonDCL.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonDCL();
                }
            }
        }
        return INSTANCE;
    }

}
