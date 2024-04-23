package sample.cluster.transformation;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.receptionist.Receptionist;
import org.apache.pekko.actor.typed.receptionist.ServiceKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import sample.cluster.CborSerializable;

//#worker
public class Worker {

  public static ServiceKey<Worker.TransformText> WORKER_SERVICE_KEY =
      ServiceKey.create(TransformText.class, "Worker");

  interface WorkerCommand extends CborSerializable {}

  public static final class TransformText implements WorkerCommand {
    public final String text;
    public final ActorRef<TextTransformed> replyTo;
    public TransformText(String text, ActorRef<TextTransformed> replyTo) {
      this.text = text;
      this.replyTo = replyTo;
    }
  }
  public static final class TextTransformed implements CborSerializable {
    public final String text;
    @JsonCreator
    public TextTransformed(String text) {
      this.text = text;
    }
  }

  public static Behavior<WorkerCommand> create() {
    return Behaviors.setup(context -> {
      context.getLog().info("Registering myself with receptionist");
      context.getSystem().receptionist().tell(Receptionist.register(WORKER_SERVICE_KEY, context.getSelf().narrow()));

      return Behaviors.receive(WorkerCommand.class)
          .onMessage(TransformText.class, command -> {
            command.replyTo.tell(new TextTransformed(command.text.toUpperCase()));
            return Behaviors.same();
          }).build();
    });
  }
}
//#worker
