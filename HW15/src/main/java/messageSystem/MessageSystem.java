package messageSystem;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author sergey
 * created on 22.07.17.
 */

@Service
public class MessageSystem {
    private static final int DEFAULT_STEP_TIME = 10;

    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap = new HashMap<>();
    private final Map<Address, ConcurrentLinkedQueue<MessageResponse>> resultMap = new ConcurrentHashMap<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public void addAddress(Address address) {
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
        messagesMap.put(address, queue);
        new Thread(() -> {
            while (true) {
                while (!queue.isEmpty()) {
                    System.out.println("number of elements in this queue:" + queue.size());
                    Message message = queue.poll();
                    MessageResponse response = message.exec();
                    resultMap.get(message.getFrom()).add(response);
                    System.out.println(new Date() + " done. msg from:" + message.getFrom() + ",  to:" + message.getTo() + ", response:" + response.toString());
                }
                try {
                    Thread.sleep(MessageSystem.DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println(new Date() + " added a new address:" + address);
    }

    public void sendMessage(Message message) {
        if (!messagesMap.containsKey(message.getTo())) {
            addAddress(message.getTo());
        }
        messagesMap.get(message.getTo()).add(message);
        resultMap.putIfAbsent(message.getFrom(), new ConcurrentLinkedQueue<>());
    }

    public MessageResponse getResponse(Address address) {
        System.out.println("ask result for:" + address);
        return resultMap.get(address).poll();
    }
}
