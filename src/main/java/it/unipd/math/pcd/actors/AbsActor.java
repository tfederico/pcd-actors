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
import it.unipd.math.pcd.actors.implementation.MsgWrapper;
import it.unipd.math.pcd.actors.implementation.AbsActorRef;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    /**
     * concurrent queue of the received mail
     */

    private BlockingQueue<MsgWrapper<T>> mailbox;

    /**
     * true if it gets terminated by actor system, false instead
     */

    private volatile boolean isTerminated;

    /**
     * true if the letter manager has been created
     */

    private volatile boolean managerCreated;

    /**
     * Sets the self-reference.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    /**
     * Public constructor of AbsActor.
     * It creates the mailbox and set to false the fields terminated
     * and managerCreated (obvious)
     */

    public AbsActor(){
        //creating a new letter-box
        mailbox = new LinkedBlockingQueue<>();
        //setting the others fields
        self=null;
        sender=null;
        isTerminated=false; //it has just been created, it can't already be terminated
        managerCreated=false; // it hasn't been created yet
    }

    /**
     * The method set the sender to the last ActorRef that has send a message
     * to the actor itself
     * @param sender sender of the messages
     */

    protected final void setSender(ActorRef<T> sender) {
        this.sender = sender;
    }

    /**
     * The method pushes a mail into the mailbox
     * @param mail Mail that has to be put in the mailbox
     * @throws NoSuchActorException if it tries to add in the mailbox a mail and the actor is terminated
     */
    public synchronized void addInMailbox(MsgWrapper<T> mail){

        try {
            if (isTerminated)
                throw new NoSuchActorException();
            else
                mailbox.put(mail);


        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (!managerCreated)
            createMailBoxManager();


    }

    /**
     * This method creates the mailboxManager.
     * It is "synchronized" because only one mailbox manager is allowed to exists
     */
    private synchronized void createMailBoxManager() {
        ((AbsActorRef<T>)self).AbsActorRefExecute(new MailBoxManager());
        managerCreated = true;
    }


    /**
     * This method set terminated true to stop the mailbox manager,
     * then it call a method to empty the mailbox, flushMailBox()
     * @return boolean, true when the actor has been stopped
     * @throws NoSuchActorException if the actor has been stopped
     */
    public boolean stop() {
        synchronized (this){
            if (!isTerminated)      //check-then-act race condition
                isTerminated = true;
            else
                throw new NoSuchActorException();
        }

        flushMailbox();
        return true;

    }

    /**
     * This method empty the mailbox of an actor
     */
    public void flushMailbox(){
        while(!(mailbox.isEmpty()))
            mailManagement();
    }

    public boolean isEmptyMailBox(){
        return mailbox.isEmpty();
    }

    /**
     * Class that implements Runnable in order to manage the message in the mailbox
     */
    private class MailBoxManager implements Runnable {

        /**
         * if the actor isn't terminated then the mailbox manager works.
         * if the actor is terminated the mailbox manager must stop.
         */

        @Override
        public void run() {
            /**
             * This could have been done with a Condition,
             * but it's useless to use a lock just for this
             * and it's more dangerous (if not used properly)
             */
            while(!isTerminated)
                mailManagement();
        }



    }

    /**
     * This method takes a mail from the mailbox, set sender of the message in the actor
     * and invoke on the receiver the receive() method
     */
    private void mailManagement(){
        try {

             MsgWrapper mail = mailbox.take();
             setSender(mail.getSender());
             receive((T) mail.getMessage());

        } catch (InterruptedException exec) {
            exec.printStackTrace();
        }
    }

}
