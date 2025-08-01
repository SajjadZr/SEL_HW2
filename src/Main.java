import edu.sharif.selab.models.EmailMessage;
import edu.sharif.selab.models.Message;
import edu.sharif.selab.models.SmsMessage;
import edu.sharif.selab.models.TelegramMessage; // افزودن ایمپورت جدید
import edu.sharif.selab.services.EmailMessageService;
import edu.sharif.selab.services.MessageService;
import edu.sharif.selab.services.SmsMessageService;
import edu.sharif.selab.services.TelegramMessageService; // افزودن ایمپورت جدید

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Hello and Welcome to SE Lab Messenger.");
        int userAnswer = 0;
        do {
            Message message = null;
            String source;
            String target;
            String content;

            // 1. افزودن گزینه تلگرام به منو
            System.out.println("In order to send Sms message enter 1");
            System.out.println("In order to send Email message enter 2");
            System.out.println("In order to send Telegram message enter 3"); // گزینه جدید
            System.out.println("In order to Exit, Enter 0");

            userAnswer = scanner.nextInt();
            scanner.nextLine(); // مصرف کردن newline باقی‌مانده

            if (userAnswer == 0) {
                break;
            }

            switch (userAnswer) {
                case 1:
                    SmsMessage smsMessage = new SmsMessage();
                    System.out.print("Enter source phone: ");
                    source = scanner.next();
                    smsMessage.setSourcePhoneNumber(source);
                    System.out.print("Enter target phone: ");
                    target = scanner.next();
                    smsMessage.setTargetPhoneNumber(target);
                    System.out.println("Write Your Message: ");
                    scanner.nextLine(); // مصرف کردن newline
                    content = scanner.nextLine();
                    smsMessage.setContent(content);
                    message = smsMessage;
                    break;
                case 2:
                    EmailMessage emailMessage = new EmailMessage();
                    System.out.print("Enter source email: ");
                    source = scanner.next();
                    emailMessage.setSourceEmailAddress(source);
                    System.out.print("Enter target email: ");
                    target = scanner.next();
                    emailMessage.setTargetEmailAddress(target);
                    System.out.println("Write Your Message: ");
                    scanner.nextLine(); // مصرف کردن newline
                    content = scanner.nextLine();
                    emailMessage.setContent(content);
                    message = emailMessage;
                    break;
                // 2. افزودن case برای تلگرام
                case 3:
                    TelegramMessage telegramMessage = new TelegramMessage();
                    System.out.print("Enter source ID/phone: ");
                    source = scanner.next();
                    telegramMessage.setSourceId(source);
                    System.out.print("Enter target ID/phone: ");
                    target = scanner.next();
                    telegramMessage.setTargetId(target);
                    System.out.println("Write Your Message: ");
                    scanner.nextLine(); // مصرف کردن newline
                    content = scanner.nextLine();
                    telegramMessage.setContent(content);
                    message = telegramMessage;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue; // ادامه به حلقه بعدی
            }

            // 3. افزودن منطق برای فراخوانی سرویس تلگرام
            try {
                if (message instanceof SmsMessage) {
                    SmsMessageService smsService = new SmsMessageService();
                    smsService.sendSmsMessage((SmsMessage) message);
                } else if (message instanceof EmailMessage) {
                    EmailMessageService emailService = new EmailMessageService();
                    emailService.sendEmailMessage((EmailMessage) message);
                } else if (message instanceof TelegramMessage) {
                    TelegramMessageService telegramService = new TelegramMessageService();
                    telegramService.sendTelegramMessage((TelegramMessage) message);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (true);

        System.out.println("Thanks for using our messenger.");
        scanner.close();
    }
}