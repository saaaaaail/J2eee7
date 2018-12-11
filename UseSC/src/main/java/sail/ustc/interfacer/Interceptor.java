package sail.ustc.interfacer;

public interface Interceptor {

    public void preAction(String sp);

    public void afterAction(String sa);
}
