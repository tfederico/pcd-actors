/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.HashMap;
import java.util.Map;

/**
 * A map-based implementation of the actor system.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActorSystem implements ActorSystem {

    /**
     * Associates every Actor created with an identifier.
     */
    protected Map<ActorRef<?>, Actor<?>> actors;

    /**
     * The constructor creates a map for the actors,
     * using their ActorRef as keys
     */
    public AbsActorSystem(){
        actors = new HashMap<>();
    }

    /**
     *
     * @param actor The type of actor that has to be created
     * @param mode The mode of the actor requested
     * @return ActorRef The ActorRef to the Actor
     */

    @Override
    public ActorRef<? extends Message> actorOf(Class<? extends Actor> actor, ActorMode mode) {

        // ActorRef instance
        ActorRef<?> reference;
        try {
            // Create the reference to the actor
            reference = this.createActorReference(mode);
            // Create the new instance of the actor
            Actor actorInstance = ((AbsActor) actor.newInstance()).setSelf(reference);
            // Associate the reference to the actor
            actors.put(reference, actorInstance);

        } catch (InstantiationException | IllegalAccessException e) {
            throw new NoSuchActorException(e);
        }
        return reference;
    }

    /**
     *
     * @param actor The type of actor that has to be created
     * @return ActorRef An ActorRef to the Actor (which can only be local)
     */

    @Override
    public ActorRef<? extends Message> actorOf(Class<? extends Actor> actor) {
        return this.actorOf(actor, ActorMode.LOCAL);
    }

    protected abstract ActorRef createActorReference(ActorMode mode);

    /**
     * This method returns the Actor associated with ActorRef ref
     * @param ref An ActorRef
     * @return Actor Returns the actor which has ActorRef as key
     * @throws NoSuchActorException
     */

    public Actor<?> findActor(ActorRef<?> ref) throws NoSuchActorException{
        Actor act = actors.get(ref);
        if(act != null)
            return act;
        else
            throw new NoSuchActorException();
    }

    /**
     * This method will take a runnable and it will execute it
     * @param r
     */
    public abstract void systemExecute(Runnable r);
}