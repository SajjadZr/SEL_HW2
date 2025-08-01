
## پیام‌رسان قزمیت :)

این یک برنامه بسیار ساده است که هر فردی، حتی با دانش ابتدایی برنامه‌نویسی، می‌تواند آن را بنویسد. هدف ما از ارائه این برنامه، آموزش عملی مفاهیم شی‌گرایی به دانشجویان محترمی است که در نیمسال بهار ۱۴۰۳ درس آزمایشگاه مهندسی نرم‌افزار را در دانشکده مهندسی کامپیوتر اخذ کرده‌اند. این دانشجویان قرار است کد اولیه این برنامه را اصلاح و بهبود دهند.

---

## مرحله اول: افزودن قابلیت ارسال پیام تلگرامی (بدون رعایت SOLID)

در این مرحله، قابلیت ارسال پیام از طریق تلگرام به نسخه اولیه برنامه افزوده شد. برای این منظور، دو کلاس جدید به پروژه اضافه شد:

* `TelegramMessage`
* `TelegramMessageService`

همچنین برای استفاده از این قابلیت، منطق مربوطه مستقیماً به کلاس `Main` افزوده شد.

---

## مرحله دوم: ثبت و شمارش تغییرات

برای افزودن قابلیت تلگرام به نسخه اولیه، تغییرات زیر در کد صورت گرفت:

| ردیف | نوع تغییر     | نام کلاس/تابع                                      |
| ---- | ------------- | -------------------------------------------------- |
| ۱    | افزودن کلاس   | `edu.sharif.selab.models.TelegramMessage`          |
| ۲    | افزودن کلاس   | `edu.sharif.selab.services.TelegramMessageService` |
| ۳    | تغییر در تابع | `main` در کلاس `Main`                              |

**تعداد کل تغییرات مورد نیاز: ۳ مورد**

---

## مرحله سوم: تحلیل اصول SOLID (نسخه اولیه پس از افزودن تلگرام)

با بررسی کلاس `Main` و کلاس‌های موجود در بسته `services`، موارد زیر از نقض اصول پنج‌گانه SOLID شناسایی شدند:

| اصل SOLID             | وضعیت     | دلیل نقض                                                                                                              |
| --------------------- | --------- | ----------------------------------------------------------------------------------------------------------------------- |
| Single Responsibility | رعایت شده | کلاس‌های سرویس تنها وظیفه ارسال پیام را دارند و کلاس‌های مدل نیز تنها داده را نگهداری می‌کنند.                        |
| Open/Closed           | نقض شده   | کلاس `Main` برای افزودن قابلیت جدید مانند تلگرام تغییر کرده و در آینده نیز باید تغییر کند.                            |
| Liskov Substitution   | نقض شده   | زیرکلاس‌هایی مانند `SmsMessageService` برخی متدهای اینترفیس را به‌صورت ناقص یا با بدنه خالی پیاده‌سازی می‌کنند.       |
| Interface Segregation | نقض شده   | اینترفیس `MessageService` متدهای زیادی دارد که همه کلاس‌ها به آن نیاز ندارند و منجر به پیاده‌سازی‌های بی‌مصرف می‌شود. |
| Dependency Inversion  | نقض شده   | کلاس `Main` مستقیماً به کلاس‌های سرویس ملموس مانند `new SmsMessageService()` وابسته است و نه به abstraction.          |

---

## مرحله چهارم: راهکارهای پیشنهادی برای اصلاح کد

برای رعایت اصول شی‌گرایی در کد، پیشنهادهای زیر ارائه می‌شود:

| اصل نقض‌شده                                 | علت نقض                                                                                                     | راهکار پیشنهادی                                                                                             |
| ------------------------------------------- | ----------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| Interface Segregation و Liskov Substitution | اینترفیس `MessageService` بیش از حد کلی است و کلاس‌های فرزند را وادار به پیاده‌سازی متدهای غیرضروری می‌کند. | تعریف یک اینترفیس ساده و عمومی به شکل `MessageService<T extends Message>` با تنها یک متد `send(T message)`  |
| Open/Closed و Dependency Inversion          | کلاس `Main` مستقیماً به کلاس‌های سرویس ملموس وابسته است و برای افزودن قابلیت جدید باید تغییر یابد.          | تعریف یک کلاس `MessageServiceFactory` که مسئول ساخت نمونه‌های سرویس است و وابستگی‌های مستقیم را حذف می‌کند. |

---

## مرحله پنجم: پیاده‌سازی نهایی مطابق اصول SOLID

در نسخه نهایی پروژه، ساختار کد بر اساس اصول SOLID بازطراحی شده است. مهم‌ترین تغییرات عبارت‌اند از:

### ۱. تعریف اینترفیس Generic

```java
public interface MessageService<T extends Message> {
    void send(T message);
}
```

### ۲. پیاده‌سازی کلاس Factory

```java
public class MessageServiceFactory {
    private final Map<String, Supplier<MessageService<?>>> registry = new HashMap<>();

    public MessageServiceFactory() {
        registry.put("sms", SmsMessageService::new);
        registry.put("email", EmailMessageService::new);
        registry.put("telegram", TelegramMessageService::new);
    }

    public MessageService<?> getService(String type) {
        Supplier<MessageService<?>> serviceSupplier = registry.get(type.toLowerCase());
        if (serviceSupplier == null) {
            throw new IllegalArgumentException("Unknown service type: " + type);
        }
        return serviceSupplier.get();
    }
}
```

### ۳. مثال از کلاس سرویس (SMS)

```java
public class SmsMessageService implements MessageService<SmsMessage> {
    @Override
    public void send(SmsMessage message) {
        if (validatePhoneNumber(message.getSourcePhoneNumber()) && validatePhoneNumber(message.getTargetPhoneNumber())) {
            System.out.println("Sending a SMS from " + message.getSourcePhoneNumber() + " to " + message.getTargetPhoneNumber() + " with content : " + message.getContent());
        } else {
            throw new IllegalArgumentException("Phone Number is Not Correct!");
        }
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) return false;
        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
```

### ۴. کد نهایی کلاس Main

```java
public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    private static final MessageServiceFactory factory = new MessageServiceFactory();

    public static void main(String[] args) {
        System.out.println("Hello and Welcome to SE Lab Messenger.");

        while (true) {
            System.out.println("
Select message type (sms, email, telegram) or type 'exit' to quit:");
            String type = scanner.nextLine().toLowerCase();

            if ("exit".equals(type)) break;

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void sendMessage(String type, Message message) {
        MessageService service = factory.getService(type);
        service.send(message);
    }
}
```

---

## سوالات پایانی

### ۱. اگر اصول شی‌گرایی از ابتدا رعایت شده بود، چند تغییر حذف می‌شد و چند تغییر لازم بود؟

در صورت رعایت اصول شی‌گرایی از ابتدا، تغییر در تابع `main` (تغییر شماره ۳ در جدول مرحله دوم) ضرورتی نداشت. بنابراین فقط سه تغییر لازم بود:

* افزودن کلاس `TelegramMessage`
* افزودن کلاس `TelegramMessageService`
* افزودن یک خط به کلاس `MessageServiceFactory` برای ثبت سرویس جدید

### ۲. رعایت اصول شی‌گرایی چه مزایایی برای پروژه شما ایجاد می‌کند؟

رعایت اصول شی‌گرایی، توسعه‌پذیری و قابلیت نگهداری پروژه را افزایش می‌دهد. با استفاده از abstraction و کاهش وابستگی‌ها، می‌توان ویژگی‌های جدید را بدون تغییر در کدهای موجود اضافه کرد. همچنین، درک، تست و استفاده مجدد از کد ساده‌تر خواهد شد.

---
