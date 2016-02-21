package it.unipd.math.pcd.actors.implementation;

/**
 * Concrete class of ActorSystem
 * Created by federico on 09/02/16.
 */

import it.unipd.math.pcd.actors.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.*;
import java.util.List;


public class ActorSystemImpl extends AbsActorSystem {

    private ExecutorService execService;

    /**
     * Constructor that initialize exService to a newCachedThreadPool,
     * necessary to execute MailBoxManager
     */
    public ActorSystemImpl(){
        execService = Executors.newCachedThreadPool();
    }

    /**
     *
     * @param mode describes the mode of the Actor (local or distributed)
     * @return ActorRef an ActorRef to the new Actor that has been created
     */

    @Override
    protected ActorRef createActorReference(ActorMode mode){
        if (mode == ActorMode.LOCAL)
            return new LocalActorRef(this);
        else
            throw new IllegalArgumentException();
    }

    /**
     * Method that makes the ActorSystem execute a Runnable
     * @param r a runnable that has to be executed
     */

    public void systemExecute(Runnable r){
        execService.execute(r);
    }

    /**
     * Inner class that implements a Callable, used to stop a single actor.
     * Using a Callable is possible to know if an actor has been stopped or not (Boolean).
     */

    private static class Stoppable implements Callable<Boolean>{

        private AbsActor<?> actor;

        Stoppable(AbsActor<?> actor){
            this.actor = actor;
        }

        @Override
        public Boolean call() throws Exception {
            return actor.stop();
        }
    }

    /**
     * The method gets the actor from the map, then it creates a future to get the actor
     * that has to be stopped.
     * At the end, it removes the actor from the map.
     * @param actorToRemove The actor to be stopped
     */

    @Override
    public void stop(ActorRef<?> actorToRemove) {
        final AbsActor<?> toStop = ((AbsActor<?>) findActor(actorToRemove));
        FutureTask<Boolean> future = new FutureTask<>(new Stoppable(toStop));
        execService.execute(future);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException except) {
            except.printStackTrace();
        }
        actors.remove(actorToRemove);
    }

    /**
     * For each actor in the map, it creates a future task in order to create Stoppable
     * that must empty the mailbox. As last action, it clears the ActorSystem actor map
     */

    @Override
    public void stop() {
        List<FutureTask<Boolean>> futureList = new LinkedList<>();
        FutureTask<Boolean> singleFutureTask;
        for (Map.Entry<ActorRef<?>, Actor<?>> singleActor : actors.entrySet()) {
            singleFutureTask = new FutureTask<>(new Stoppable((AbsActor<?>) singleActor.getValue()));
            futureList.add(singleFutureTask);
            execService.execute(singleFutureTask);
        }
        for (FutureTask<Boolean> checkFutureTask : futureList) {
            try {
                checkFutureTask.get();
            } catch (InterruptedException | ExecutionException exec) {
                exec.printStackTrace();
            }
        }
        actors.clear();
    }
}
