package melmac.core.threading;

import java.util.ArrayList;

/**
 * Provides a very simple publisher/subscriber mechanism.
 * Used by the UI/Vision->WorldStateProvider to notify the Agent whenever the state has changed.
 * Notifiers should extent or encapsulate an instance of this class.
 * Subscribers should implement the Subscriber interface and register themselves with the target notifier using addSubscriber.
 */
public class BasicNotifier implements Notifier
{

    private final ArrayList<Subscriber> subscriberList = new ArrayList<Subscriber>();

    /**
     * Registers a subscriber with this notifier. The subscriber will be notified whenever the publisher calls notifySubscribers().
     */
    @Override
    public synchronized void addSubscriber(Subscriber subscriber)
    {
        subscriberList.add(subscriber);
    }

    /**
     * Removes a subscriber from the list; probably never used in practice.
     */
    @Override
    public synchronized void removeSubscriber(Subscriber subscriber)
    {
        subscriberList.remove(subscriber);
    }

    /**
     * Called by the publisher to notify all registered subscribers. What the subscriber does when it receives a signal is
     * up to the subscriber but will usually involve waking a waiting thread.
     */
    public synchronized void notifySubscribers()
    {
        for (Subscriber subscriber : subscriberList)
        {
            subscriber.signal();
        }
    }
}
