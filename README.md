<!-- # پیام رسان قزمیت :)
این یک برنامه خیلی کوچکی است که هر دوست عزیزی با هر سوادی (حتی سواد اول دبستان .. خخخ) می‌تواند بنویسد. ما قصد داریم این برنامه را به منظور یادگیری اصول شی گرایی به صورت عملی در اختیار دانشجویان عزیز و فرهیخته ای که در دانشکده مهندسی کامپیوتر و در بهار 1403 درس آز مهندسی نرم افزار را اخذ کرده اند قرار دهیم تا آن را اصلاح کنند.

---

## مرحله دوم: ثبت و شمارش تغییرات

برای افزودن قابلیت ارسال پیام با تلگرام به نسخه اولیه، تغییرات زیر در کد ایجاد شد.

| ردیف | نوع تغییر | نام کلاس/تابع |
| :--- | :--- | :--- |
| ۱ | افزودن کلاس | `edu.sharif.selab.models.TelegramMessage` |
| ۲ | افزودن کلاس | `edu.sharif.selab.services.TelegramMessageService` |
| ۳ | تغییر در تابع | `main` در کلاس `Main` |

**تعداد کل تغییرات مورد نیاز: ۳**

---

## مرحله سوم: تحلیل اصول SOLID (قبل از اصلاح)

بررسی کلاس `Main` و کلاس‌های بسته `services` در نسخه اولیه (پس از افزودن تلگرام بدون رعایت اصول) موارد نقض زیر را نشان می‌دهد.

| اصل SOLID | وضعیت | دلیل نقض |
| :--- | :--- | :--- |
| **Open/Closed Principle** | 👎 **نقض** | کلاس `Main` برای افزودن قابلیت جدید (تلگرام) مستقیماً ویرایش شد و برای قابلیت‌های بعدی نیز باید تغییر کند. |
| **Liskov Substitution Principle**| 👎 **نقض** | زیرکلاس‌های `MessageService` (مانند `SmsMessageService`) رفتار متدهای اینترفیس را به طور کامل پیاده‌سازی نمی‌کنند (بدنه‌های خالی دارند) که منجر به رفتار غیرمنتظره می‌شود. |
| **Interface Segregation Principle**| 👎 **نقض** | اینترفیس `MessageService` یک "اینترفیس فربه" است و کلاس‌ها را مجبور به پیاده‌سازی متدهایی می‌کند که به آن‌ها نیازی ندارند. |
| **Dependency Inversion Principle**| 👎 **نقض** | کلاس `Main` (ماژول سطح بالا) به طور مستقیم به کلاس‌های سرویس ملموس مانند `new SmsMessageService()` (ماژول‌های سطح پایین) وابسته است. |

---

## مرحله چهارم: ارائه راهکار برای اصلاح کد

برای برقراری اصول نقض شده، راهکارهای زیر پیشنهاد می‌شود.

| اصل نقض شده | علت نقض | راهکار پیشنهادی |
| :--- | :--- | :--- |
| **ISP** & **LSP** | اینترفیس `MessageService` فربه و کلی است و باعث ایجاد متدهای خالی در کلاس‌های فرزند می‌شود. | **شکستن اینترفیس فربه.** <br> ایجاد یک اینترفیس Generic به نام `MessageService<T extends Message>` با یک متد واحد `send(T message)`. |
| **OCP** & **DIP** | `Main` به کلاس‌های ملموس وابسته است و برای افزودن سرویس جدید باید کد آن تغییر کند. | **استفاده از الگوی طراحی Factory.** <br> ایجاد کلاس `MessageServiceFactory` برای کپسوله کردن منطق ساخت سرویس‌ها و حذف وابستگی مستقیم `Main` به کلاس‌های سرویس ملموس. |

---

## مرحله پنجم: پیاده‌سازی نهایی با اصول SOLID

کد زیر نسخه نهایی و اصلاح شده پروژه است که در آن اصول SOLID رعایت شده‌اند. ✅

### ۱. اینترفیس `MessageService` (Generic)
این اینترفیس جدید، کوچک و متمرکز است و مشکلات ISP و LSP را حل می‌کند.

```java
package edu.sharif.selab.services;

import edu.sharif.selab.models.Message;

public interface MessageService<T extends Message> {
    void send(T message);
} -->


# پیام رسان قزمیت :)
این یک برنامه خیلی کوچکی است که هر دوست عزیزی با هر سوادی (حتی سواد اول دبستان .. خخخ) می‌تواند بنویسد. ما قصد داریم این برنامه را به منظور یادگیری اصول شی گرایی به صورت عملی در اختیار دانشجویان عزیز و فرهیخته ای که در دانشکده مهندسی کامپیوتر و در بهار 1403 درس آز مهندسی نرم افزار را اخذ کرده اند قرار دهیم تا آن را اصلاح کنند.

---

## مرحله اول: افزودن قابلیت تلگرام (بدون SOLID)
در این مرحله، قابلیت ارسال پیام از طریق تلگرام به ساختار موجود اضافه شد. برای این کار، کلاس‌های `TelegramMessage` و `TelegramMessageService` ایجاد و منطق لازم به کلاس `Main` افزوده شد.

## مرحله دوم: ثبت و شمارش تغییرات
برای افزودن قابلیت ارسال پیام با تلگرام به نسخه اولیه، تغییرات زیر در کد ایجاد شد.

| ردیف | نوع تغییر | نام کلاس/تابع |
| :--- | :--- | :--- |
| ۱ | افزودن کلاس | `edu.sharif.selab.models.TelegramMessage` |
| ۲ | افزودن کلاس | `edu.sharif.selab.services.TelegramMessageService` |
| ۳ | تغییر در تابع | `main` در کلاس `Main` |

**تعداد کل تغییرات مورد نیاز: ۳**

---

## مرحله سوم: تحلیل اصول SOLID (قبل از اصلاح)
بررسی کلاس `Main` و کلاس‌های بسته `services` در نسخه اولیه (پس از افزودن تلگرام بدون رعایت اصول) موارد نقض زیر را نشان می‌دهد.

| اصل SOLID | وضعیت | دلیل نقض |
| :--- | :--- | :--- |
| **Open/Closed Principle** | 👎 **نقض** | کلاس `Main` برای افزودن قابلیت جدید (تلگرام) مستقیماً ویرایش شد و برای قابلیت‌های بعدی نیز باید تغییر کند. |
| **Liskov Substitution Principle**| 👎 **نقض** | زیرکلاس‌های `MessageService` (مانند `SmsMessageService`) رفتار متدهای اینترفیس را به طور کامل پیاده‌سازی نمی‌کنند (بدنه‌های خالی دارند) که منجر به رفتار غیرمنتظره می‌شود. |
| **Interface Segregation Principle**| 👎 **نقض** | اینترفیس `MessageService` یک "اینترفیس فربه" است و کلاس‌ها را مجبور به پیاده‌سازی متدهایی می‌کند که به آن‌ها نیازی ندارند. |
| **Dependency Inversion Principle**| 👎 **نقض** | کلاس `Main` (ماژول سطح بالا) به طور مستقیم به کلاس‌های سرویس ملموس مانند `new SmsMessageService()` (ماژول‌های سطح پایین) وابسته است. |

---

## مرحله چهارم: ارائه راهکار برای اصلاح کد
برای برقراری اصول نقض شده، راهکارهای زیر پیشنهاد می‌شود.

| اصل نقض شده | علت نقض | راهکار پیشنهادی |
| :--- | :--- | :--- |
| **ISP** & **LSP** | اینترفیس `MessageService` فربه و کلی است و باعث ایجاد متدهای خالی در کلاس‌های فرزند می‌شود. | **شکستن اینترفیس فربه.** <br> ایجاد یک اینترفیس Generic به نام `MessageService<T extends Message>` با یک متد واحد `send(T message)`. |
| **OCP** & **DIP** | `Main` به کلاس‌های ملموس وابسته است و برای افزودن سرویس جدید باید کد آن تغییر کند. | **استفاده از الگوی طراحی Factory.** <br> ایجاد کلاس `MessageServiceFactory` برای کپسوله کردن منطق ساخت سرویس‌ها و حذف وابستگی مستقیم `Main` به کلاس‌های سرویس ملموس. |

---

## مرحله پنجم: پیاده‌سازی نهایی با اصول SOLID
کد زیر نسخه نهایی و اصلاح شده پروژه است که در آن اصول SOLID رعایت شده‌اند. ✅

### ۱. اینترفیس `MessageService` (Generic)
این اینترفیس جدید، کوچک و متمرکز است و مشکلات ISP و LSP را حل می‌کند.

```java
package edu.sharif.selab.services;

import edu.sharif.selab.models.Message;

public interface MessageService<T extends Message> {
    void send(T message);
}

۲. کلاس MessageServiceFactory
این کلاس جدید با کپسوله کردن منطق ساخت سرویس‌ها، مشکلات OCP و DIP را برطرف می‌کند.

package edu.sharif.selab.services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

۳. نمونه‌ای از یک کلاس سرویس اصلاح شده (SmsMessageService)
این کلاس اکنون فقط اینترفیس مربوط به خود را با یک متد پیاده‌سازی می‌کند. کلاس‌های EmailMessageService و TelegramMessageService نیز به همین شکل اصلاح می‌شوند.

package edu.sharif.selab.services;

import edu.sharif.selab.models.SmsMessage;

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
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }
        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

۴. کلاس Main نهایی
کلاس Main اکنون وابستگی به کلاس‌های سرویس ملموس ندارد و منطق آن بسیار ساده‌تر و تمیزتر شده است.

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void sendMessage(String type, Message message) {
        MessageService service = factory.getService(type);
        service.send(message);
    }
}
