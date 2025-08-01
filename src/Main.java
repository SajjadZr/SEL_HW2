import edu.sharif.selab.models.EmailMessage;
import edu.sharif.selab.models.Message;
import edu.sharif.selab.models.SmsMessage;
import edu.sharif.selab.models.TelegramMessage;
import edu.sharif.selab.services.MessageService;
import edu.sharif.selab.services.MessageServiceFactory;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    private static final MessageServiceFactory factory = new MessageServiceFactory();

    public static void main(String[] args) {
        System.out.println("Hello and Welcome to SE Lab Messenger.");
        
        while (true) {
            System.out.println("\nSelect message type (sms, email, telegram) or type 'exit' to quit:");
            String type = scanner.nextLine().toLowerCase();

            if ("exit".equals(type)) {
                break;
            }

            try {
                Message message = createMessage(type);
                if (message == null) continue;

                sendMessage(type, message);

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        System.out.println("Thanks for using our messenger.");
        scanner.close();
    }

    private static Message createMessage(String type) {
        switch (type) {
            case "sms":
                SmsMessage sms = new SmsMessage();
                System.out.print("Enter source phone: ");
                sms.setSourcePhoneNumber(scanner.nextLine());
                System.out.print("Enter target phone: ");
                sms.setTargetPhoneNumber(scanner.nextLine());
                System.out.println("Write Your Message: ");
                sms.setContent(scanner.nextLine());
                return sms;
            case "email":
                EmailMessage email = new EmailMessage();
                System.out.print("Enter source email: ");
                email.setSourceEmailAddress(scanner.nextLine());
                System.out.print("Enter target email: ");
                email.setTargetEmailAddress(scanner.nextLine());
                System.out.println("Write Your Message: ");
                email.setContent(scanner.nextLine());
                return email;
            case "telegram":
                TelegramMessage telegram = new TelegramMessage();
                System.out.print("Enter source ID/phone: ");
                telegram.setSourceId(scanner.nextLine());
                System.out.print("Enter target ID/phone: ");
                telegram.setTargetId(scanner.nextLine());
                System.out.println("Write Your Message: ");
                telegram.setContent(scanner.nextLine());
                return telegram;
            default:
                System.out.println("Invalid message type.");
                return null;
        }
    }

    // Suppress warnings for unchecked and raw type usage, which is intentional here.
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void sendMessage(String type, Message message) {
        // By using the raw type "MessageService", we bypass the compile-time generic check.
        // We know this is safe because we create the service and message using the same "type" string.
        MessageService service = factory.getService(type);
        service.send(message);
    }
}
