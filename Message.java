import java.util.Scanner;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class Message {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your message: ");
        String message = scanner.nextLine().trim();

        int count = getIntInput(scanner, "How many times do you want to send the message? ", 1, 10000);

        int delaySeconds = getIntInput(scanner, "Delay between messages (in seconds): ", 0, 60);
        int delay = delaySeconds * 1000; // convert to milliseconds

        int startDelay = getIntInput(scanner, "Initial delay before starting (in seconds): ", 1, 30);

        try {
            copyToClipboard(message);
            System.out.println("\n✅ Switch to your target window now...");
            Thread.sleep(startDelay * 1000);

            sendMessages(count, delay);
            System.out.println("\n✅ All messages sent successfully!");

        } catch (AWTException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input < min || input > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear invalid input
            }
        }
        scanner.nextLine(); // consume leftover newline
        return input;
    }

    private static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }

    private static void sendMessages(int count, int delay) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        robot.setAutoDelay(50); // small delay between every key event for reliability

        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        int modifierKey = isMac ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

        for (int i = 1; i <= count; i++) {
            // Paste (Ctrl+V or Cmd+V)
            robot.keyPress(modifierKey);
            robot.keyPress(KeyEvent.VK_V);
            Thread.sleep(50); // ensure paste happens
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(modifierKey);

            Thread.sleep(100); // wait for text to appear

            // Press Enter
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(50);
            robot.keyRelease(KeyEvent.VK_ENTER);

            System.out.println("✅ Message " + i + " sent.");
            Thread.sleep(delay);
        }
    }
}
