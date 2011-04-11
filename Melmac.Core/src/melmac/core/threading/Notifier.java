package melmac.core.threading;

public interface Notifier
{

    void addSubscriber(Subscriber subscriber);

    void removeSubscriber(Subscriber subscriber);
}
